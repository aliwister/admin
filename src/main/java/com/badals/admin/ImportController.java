package com.badals.admin;

import com.badals.admin.service.ShipmentService;
import com.badals.admin.service.TrackingService;
import com.badals.admin.service.mutation.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.ResponseMessage;

import javax.servlet.http.HttpServletResponse;
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

/*
    @RequestMapping(method= RequestMethod.GET,path = "amazon-tracking")
    public Message processAmazonShipments() throws IOException {
        return trackingService.processAmazonFile(file);
    }
*/

    @GetMapping(
        value = "/detrack-label",
        produces = MediaType.APPLICATION_PDF_VALUE
    )
    public @ResponseBody byte[] downloadLabel(@RequestParam("id") String id, HttpServletResponse response) throws IOException {
        byte[] file = shipmentService.downloadLabel(id);
        return file;
    }

    @PostMapping("/amazon-file" )
    @ResponseBody
    public String uploadFile(@RequestPart("file") MultipartFile file) {
        String message = "";
        try {
            //storageService.save(file);
            trackingService.processAmazonFile(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return message;
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return message;
        }
    }
}
