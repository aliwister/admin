package com.badals.admin;

import com.badals.admin.service.ShipmentService;
import com.badals.admin.service.TrackingService;
import com.badals.admin.service.mutation.Message;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/api/import")
public class ImportController {
    final
    TrackingService trackingService;
    final
    ShipmentService shipmentService;

    public ImportController(TrackingService trackingService, ShipmentService shipmentService) {
        this.trackingService = trackingService;
        this.shipmentService = shipmentService;
    }

    @RequestMapping(method= RequestMethod.GET,path = "amazon-tracking")
    public Message processAmazonShipments() throws IOException {
        return trackingService.processAmazonFile();
    }

    @GetMapping(
        value = "/detrack-label",
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public @ResponseBody byte[] downloadLabel(@RequestParam("id") String id, HttpServletResponse response) throws IOException {
        byte[] file = shipmentService.downloadLabel(id);
        return file;
    }

}
