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
        public ResponseEntity<RentalInfoListResponse> getRentalInfoList(Integer limit) {
            RentalInfoListResponse rentalInfoListResponse = new RentalInfoListResponse();
            List<ServiceError> serviceErrors = new ArrayList<>();
            rentalInfoListResponse.setErrors(serviceErrors);

            List<RentalInfo> rentalInfos = new ArrayList<>();
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

            RentalInfo rentalInfo = rentalInfoDao.findById(rental_info_id);

            if(rentalInfo == null) {
                log.error("Rental Info not exist for Id: " + rental_info_id);
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
            rentalInfoDao.save(rentalInfo);
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
            rentalInfoDao.save(rentalInfo);

            rentalInfoDetailResponse.setRentalInfo(rentalInfo);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(rentalInfoDetailResponse);
        }
}
