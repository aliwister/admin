package com.badals.admin;

import com.badals.admin.service.TrackingService;
import com.badals.admin.service.mutation.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

@Controller
@RequestMapping("/amazon-tracking")
public class ImportController {
    final
    TrackingService trackingService;

    public ImportController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @RequestMapping(method= RequestMethod.GET)
    public Message processAmazonShipments() throws IOException {
        return trackingService.processAmazonFile();
    }
}
