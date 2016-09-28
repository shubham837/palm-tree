package crossover.controller;

import crossover.dao.RentalInfoDao;
import crossover.dao.RentalInfoSolrDao;
import crossover.errors.ServiceError;
import crossover.models.RentalInfo;
import crossover.responses.RentalInfoDetailResponse;
import crossover.responses.RentalInfoListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.SolrPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by shubham.singhal on 26/09/16.
 */
@RestController
public class RentalInfoController {
        private static final Logger log = LoggerFactory.getLogger(RentalInfoController.class);

        @Autowired
        private RentalInfoDao rentalInfoDao;

        @Autowired
        private RentalInfoSolrDao rentalInfoSolrDao;

        @RequestMapping(value = "/rental-info",method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<RentalInfoListResponse> getRentalInfoList(
                @RequestParam(value = "general_term", required = false) String generalTerm,
                @RequestParam(value = "type", required = false) String type,
                @RequestParam(value = "city", required = false) String city,
                @RequestParam(value = "province", required = false) String province,
                @RequestParam(value = "country", required = false) String country,
                @RequestParam(value = "zip_code", required = false) String zipCode,
                @RequestParam(value = "has_air_condition", required = false) Boolean hasAirCondition,
                @RequestParam(value = "has_garden", required = false) Boolean hasGarden,
                @RequestParam(value = "has_pool", required = false) Boolean hasPool,
                @RequestParam(value = "is_close_to_beach", required = false) Boolean isCloseToBeach,
                @RequestParam(value = "daily_price_lt", required = false) Double dailyPriceLesserThan,
                @RequestParam(value = "daily_price_gt", required = false) Double dailyPriceGreaterThan,
                @RequestParam(value = "rooms_number_lt", required = false) Double roomsNumberLesserThan,
                @RequestParam(value = "rooms_number_gt", required = false) Double roomsNumberGreaterThan) {

            RentalInfoListResponse rentalInfoListResponse = new RentalInfoListResponse();
            List<ServiceError> serviceErrors = new ArrayList<>();
            rentalInfoListResponse.setErrors(serviceErrors);

            boolean shouldFallbackToDatabase = false;
            List<RentalInfo> rentalInfos;
            try {
                if(generalTerm != null) {
                    rentalInfos = rentalInfoSolrDao.findByGeneralSearchQuery(generalTerm, new SolrPageRequest(1, 20));
                    rentalInfoListResponse.setRentalInfos(rentalInfos);
                } else if(type != null || city != null || province != null || country != null ||
                          zipCode != null || hasAirCondition != null || hasGarden != null || hasPool != null ||
                          isCloseToBeach != null || dailyPriceLesserThan != null || dailyPriceGreaterThan != null ||
                          roomsNumberLesserThan != null || roomsNumberGreaterThan != null) {
                    rentalInfos = searchSolrForRentalInfos(type, city, province, country, zipCode,
                            hasAirCondition, hasGarden, hasPool, isCloseToBeach,
                            dailyPriceGreaterThan, dailyPriceLesserThan, roomsNumberGreaterThan, roomsNumberLesserThan);
                    rentalInfoListResponse.setRentalInfos(rentalInfos);
                } else {
                    log.info("No search query mentioned in api call, fetching first page");
                    Page<RentalInfo> rentalInfos1= rentalInfoSolrDao.findAll(new SolrPageRequest(1, 20));
                    rentalInfos = new ArrayList<>();
                    rentalInfos1.forEach(e -> rentalInfos.add(e));

                    // Here instead of returning solr results cassandra rows could be fetched
                    // shouldFallbackToDatabase = true;
                }
            }catch (Exception e) {
                log.error("Exception in fetching rental infos from solr, Error: {}", e.getMessage());
                shouldFallbackToDatabase = true;
            }

            List<RentalInfo> rentalInfoListFromDB = new ArrayList<>();
            if(shouldFallbackToDatabase) {
                try {
                    rentalInfoDao.findAll().forEach(e -> rentalInfoListFromDB.add(e));
                } catch (Exception e) {
                    log.error("Exception in fetching rental infos, Exception: " + e.getMessage());
                    serviceErrors.add(new ServiceError(0, "internal server error"));
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(rentalInfoListResponse);
                }
                rentalInfoListResponse.setRentalInfos(rentalInfoListFromDB);
            }

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(rentalInfoListResponse);
        }

        @RequestMapping(value = "/rental-info/{rental_info_id}",method = RequestMethod.GET)
        @ResponseBody
        public ResponseEntity<RentalInfoDetailResponse> getRentalInfoDetail(@RequestAttribute UUID userId, @PathVariable String rental_info_id) {
            RentalInfoDetailResponse rentalInfoDetailResponse = new RentalInfoDetailResponse();
            List<ServiceError> serviceErrors = new ArrayList<>();
            rentalInfoDetailResponse.setErrors(serviceErrors);

            RentalInfo rentalInfo = null;
            try {
                rentalInfo = rentalInfoDao.findById(rental_info_id);
            }catch (Exception e){
                log.error("Error in fetching rental info from database, RentalInfoId: {}, Error: ", rental_info_id, e.getMessage());
                serviceErrors.add(new ServiceError(1, "Error in fetching Rental Info"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(rentalInfoDetailResponse);
            }

            if(rentalInfo == null) {
                log.error("Rental Info not exist for Id: " + rental_info_id);
                serviceErrors.add(new ServiceError(2, "Rental Info not found"));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(rentalInfoDetailResponse);
            }

            rentalInfoDetailResponse.setRentalInfo(rentalInfo);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(rentalInfoDetailResponse);
        }

        @RequestMapping(value = "/rental-info",method = RequestMethod.POST)
        @ResponseBody
        public ResponseEntity<RentalInfoDetailResponse> postRentalInfo(@RequestAttribute UUID userId, @RequestBody RentalInfo rentalInfo) {
            RentalInfoDetailResponse rentalInfoDetailResponse = new RentalInfoDetailResponse();
            List<ServiceError> serviceErrors = new ArrayList<>();
            rentalInfoDetailResponse.setErrors(serviceErrors);

            rentalInfo.setCreatedTs(new Date());
            rentalInfo.setUpdatedTs(new Date());
            rentalInfo.setId(UUID.randomUUID().toString());

            rentalInfo.setLastModifiedBy(userId.toString());
            try {
                log.info("Creating rental info in database");
                rentalInfoDao.save(rentalInfo);
            }catch (Exception e) {
                log.error("Exception in updating creating rental info in database, Error: {}", e.getMessage());
                serviceErrors.add(new ServiceError(1, "Error in saving Rental Info"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(rentalInfoDetailResponse);
            }

            try {
                rentalInfoSolrDao.save(rentalInfo);
                log.info("Indexed Rental Info to Solr");
            } catch (Exception e) {
                log.error("Error in indexing Rental Info to Solr", e.getMessage());
            }

            rentalInfoDetailResponse.setRentalInfo(rentalInfo);
            return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(rentalInfoDetailResponse);
        }

        @RequestMapping(value = "/rental-info/{rental_info_id}",method = RequestMethod.PUT)
        @ResponseBody
        public ResponseEntity<RentalInfoDetailResponse> putRentalInfo(@RequestAttribute UUID userId, @PathVariable String rental_info_id,
                                                                        @RequestBody RentalInfo rentalInfo) {
            RentalInfoDetailResponse rentalInfoDetailResponse = new RentalInfoDetailResponse();
            List<ServiceError> serviceErrors = new ArrayList<>();
            rentalInfoDetailResponse.setErrors(serviceErrors);

            rentalInfo.setUpdatedTs(new Date());
            rentalInfo.setId(rental_info_id);

            try {
                log.info("Updating rental info in database, RentalInfoId: {}", rental_info_id);
                rentalInfoDao.save(rentalInfo);
            }catch (Exception e) {
                log.error("Exception in updating existing rental info in database, RentalInfoId: {}, Error: {}", rental_info_id, e.getMessage());
                serviceErrors.add(new ServiceError(1, "Error in saving Rental Info"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(rentalInfoDetailResponse);
            }

            try {
                log.info("Updating Index for existing rental Info RentalInfoId: {}", rental_info_id);
                rentalInfoSolrDao.save(rentalInfo);
            } catch (Exception e) {
                log.error("Exception in updating index for existing rental info {}", e.getMessage());
            }

            rentalInfoDetailResponse.setRentalInfo(rentalInfo);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(rentalInfoDetailResponse);
        }

        private List<RentalInfo> searchSolrForRentalInfos(String type, String city, String province, String country, String zipcode,
                                           Boolean hasAirCondition, Boolean hasGarden, Boolean hasPool, Boolean isCloseToBeach,
                                           Double dailyPriceLow, Double dailyPriceHigh, Double roomsNumberLow, Double roomsNumberHigh) {
            List<RentalInfo> rentalInfos = new ArrayList<>();
            try {
                rentalInfos = rentalInfoSolrDao.findByNamedQuery(getParameter(type),
                        getParameter(city), getParameter(province), getParameter(country), getParameter(zipcode), getParameter(hasAirCondition),
                        getParameter(hasGarden), getParameter(hasPool), getParameter(isCloseToBeach), getParameter(dailyPriceLow), getParameter(dailyPriceHigh),
                        getParameter(roomsNumberLow), getParameter(roomsNumberHigh));
            }catch (Exception e){
                log.error("Error in constructing search query for solr, Error: {}", e.getMessage());
            }
            return rentalInfos;
        }

        private String getParameter(Object value) {
            if(value == null) {
                return "*";
            }

            return value.toString();
        }
}
