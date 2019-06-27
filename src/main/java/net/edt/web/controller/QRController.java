package net.edt.web.controller;

import net.edt.web.domain.Meeting;
import net.edt.web.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/qrcode")
public class QRController {

    @Autowired
    private QRService qrService;

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> produceQRCode(@RequestBody Meeting meeting) {
        byte[] qr = qrService.generateQRCode(meeting.getDate().toString());
        return new ResponseEntity<>(qr, HttpStatus.OK);
    }

}
