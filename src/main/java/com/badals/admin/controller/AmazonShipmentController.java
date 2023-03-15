package com.badals.admin.controller;

import com.badals.admin.service.ShipmentDocService;
import com.badals.admin.service.ShipmentService;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

@Controller
@RequestMapping("/amazon/adfadf32423423423")
public class AmazonShipmentController {
   private final Logger log = LoggerFactory.getLogger(AmazonShipmentController.class);
   private final ShipmentService shipmentService;
   private final ShipmentDocService shipmentDocService;

   public AmazonShipmentController(ShipmentService shipmentService, ShipmentDocService shipmentDocService) {
      this.shipmentService = shipmentService;
      this.shipmentDocService = shipmentDocService;

   }

   @RequestMapping(path="/shipment", method= RequestMethod.POST, consumes={MediaType.APPLICATION_XML_VALUE})
   public ResponseEntity<Void> shipment(@RequestBody JsonNode json) throws IOException, URISyntaxException, ParseException {
/*
      //_emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", json);
      log.info("detrack-------------------------------------------------------------------------------------------");
      ObjectMapper mapper = new ObjectMapper();
      DetrackDelivery confirmation = mapper.readValue(json, DetrackDelivery.class);
      log.info(confirmation.getStatus() + " " + confirmation.get_do() + " " + confirmation.getView_signature_url());
      log.info("detrack-------------------------------------------------------------------------------------------2");
      updateDelivery(confirmation);
      //  _emailService.sendMail("sales@badals.com", "ali@badals.com", "Post detrack", confirmation.getAddress());*/
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_XML);

      HttpEntity<JsonNode> entity = new HttpEntity<JsonNode>(json,headers);
      ResponseEntity<byte[]> response = restTemplate.postForEntity("https://webhook.site/4c3eb6d4-3ddf-4f15-b040-81dc9a80181e", entity, byte[].class);
      return ResponseEntity.ok().build();//.status(HttpStatus.FOUND).location(URI.create("https://webhook.site/0603401a-4e8f-42be-b3df-7f2bc3c9335b")).build();
   }
}