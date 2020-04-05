package com.badals.admin.controller;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.badals.admin.config.WebConfigurer;
import com.badals.admin.domain.Shipment;
import com.badals.admin.domain.enumeration.OrderState;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.pojo.DetrackDelivery;
import com.badals.admin.repository.ShipmentDocRepository;
import com.badals.admin.service.ShipmentDocService;
import com.badals.admin.service.ShipmentService;
import com.badals.admin.service.dto.ShipmentDTO;
import com.badals.admin.service.dto.ShipmentDocDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;


@Controller
@RequestMapping("/detrack")
public class DetrackController {
    @Autowired
    ShipmentService shipmentService;

    @Autowired
    ShipmentDocService shipmentDocService;

    private final Logger log = LoggerFactory.getLogger(DetrackController.class);

    @RequestMapping(method= RequestMethod.POST)
    public String index(@RequestParam("json") String json) throws IOException, URISyntaxException, ParseException {

        //_emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", json);
        log.info("detrack-------------------------------------------------------------------------------------------");
        ObjectMapper mapper = new ObjectMapper();
        DetrackDelivery confirmation = mapper.readValue(json, DetrackDelivery.class);
        log.info(confirmation.getStatus() + " " + confirmation.get_do() + " " + confirmation.getView_signature_url());
        log.info("detrack-------------------------------------------------------------------------------------------2");
        updateDelivery(confirmation);


        //  _emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", confirmation.getAddress());
        return "";
    }

    public void updateDelivery(DetrackDelivery confirmation) {
        // TODO Auto-generated method stub

        log.info("DELIVERY SERVICE-------------------------------------------------------------------------------------------2");
        Long shipmentId = Long.parseLong(confirmation.get_do().split("-")[0]);
        //Integer shipmentId = Integer.parseInt(confirmation.get_do().split("-")[1]);

        if(confirmation.getStatus().equalsIgnoreCase("not delivered")) {
            shipmentService.setStatus(shipmentId, ShipmentStatus.FAILED);
            return;
        }

        ShipmentStatus deliveryType;
        shipmentService.setStatus(shipmentId, ShipmentStatus.DELIVERED);

        String fileKey = "pod/"+confirmation.get_do()+".pdf";
        String imageKey = "pod/"+confirmation.get_do()+".jpg";

        this.detrackPhotoUploadToS3(confirmation, fileKey,POD_URL);

        ShipmentDTO shipment = shipmentService.findOne(shipmentId).get();
        if(shipment.getProgressDone().equals(shipment.getProgressTotal()))
            shipmentService.updateOrder(shipment.getReference(), OrderState.DELIVERED);
        else
            shipmentService.updateOrder(shipment.getReference(), OrderState.PARTIALLY_DELIVERED);

        detrackPhotoUploadToS3(confirmation, fileKey, POD_URL);
        detrackPhotoUploadToS3(confirmation, imageKey, PHOTO1_URL);

        shipmentDocService.save(new ShipmentDocDTO(fileKey, shipmentId));
        shipmentDocService.save(new ShipmentDocDTO(imageKey, shipmentId));
    }

    final static String PHOTO1_URL = "https://app.detrack.com/api/v1/deliveries/photo_1.json";
    final static String POD_URL = "https://app.detrack.com/api/v1/deliveries/export.pdf";

    public void detrackPhotoUploadToS3(DetrackDelivery confirmation, String fileKey, String url) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("key", "530e610169c28ce227a9716ba28a386cedc6d2dbefef67eb");

        //String date = "2017-05-02", _do = "43784-7468";
        String json = "{\"date\":\""+ confirmation.getDate() +"\",\"do\":\""+ confirmation.get_do() +"\"}";

        params.add("json", json);
        params.add("url", url);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> response = restTemplate.postForEntity(url, params, byte[].class);

        if (response.getStatusCode() != HttpStatus.OK) {
            //Log error
        }
        AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials("AKIAIG3DJ4KLYNPBH2EA","NrbxDnWRNyUWr15H+6YgXR6KylYTa0NWt32obKLq"));
        Long contentLength = (long) response.getBody().length;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);

        //String keyName = "pod/"+confirmation.get_do()+".jpg";
        String bucketName = "badals-content";
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, new ByteArrayInputStream(response.getBody()), metadata);
        PutObjectResult putObject = s3client.putObject(putObjectRequest);


    }
}
