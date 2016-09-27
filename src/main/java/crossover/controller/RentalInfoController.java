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
                @RequestParam(value = "type", required = false) String type,
                @RequestParam(value = "city", required = false) String city,
                @RequestParam(value = "province", required = false) String province,
                @RequestParam(value = "country", required = false) String country,
                @RequestParam(value = "zip_code", required = false) String zip_code,
                @RequestParam(value = "has_air_condition", required = false) Boolean has_air_condition,
                @RequestParam(value = "has_garden", required = false) Boolean has_garden,
                @RequestParam(value = "has_pool", required = false) Boolean has_pool,
                @RequestParam(value = "is_close_to_beach", required = false) Boolean is_close_to_beach,
                @RequestParam(value = "daily_price_lt", required = false) Double daily_price_lesser_than,
                @RequestParam(value = "daily_price_gt", required = false) Double daily_price_greater_than,
                @RequestParam(value = "rooms_number_lt", required = false) Double rooms_number_lesser_than,
                @RequestParam(value = "rooms_number_gt", required = false) Double rooms_number_greater_than) {

            RentalInfoListResponse rentalInfoListResponse = new RentalInfoListResponse();
            List<ServiceError> serviceErrors = new ArrayList<>();
            rentalInfoListResponse.setErrors(serviceErrors);

            List<RentalInfo> rentalInfos = new ArrayList<>();

            //rentalInfoSolrDao.findByNamedQuery();

            try {
                rentalInfoDao.findAll().forEach(e -> rentalInfos.add(e));
            } catch (Exception e) {
                log.error("Exception in fetching rental infos, Exception: " + e.getMessage());
                serviceErrors.add(new ServiceError(0, "internal server error"));
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(rentalInfoListResponse);
            }

            rentalInfoListResponse.setRentalInfos(rentalInfos);
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
}
