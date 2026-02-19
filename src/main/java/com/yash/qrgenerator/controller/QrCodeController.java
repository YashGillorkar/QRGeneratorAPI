package com.yash.qrgenerator.controller;

import com.yash.qrgenerator.service.QrCodeService;
import com.yash.qrgenerator.service.impl.QrCodeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    @Autowired
    private  QrCodeService qrCodeService;

    @GetMapping("/url")
    public ResponseEntity<byte[]> fromUrl(@RequestParam String url) throws Exception {
        return image(qrCodeService.generateFromUrl(url));
    }

    @GetMapping("/text")
    public ResponseEntity<byte[]> fromText(@RequestParam String text) throws Exception {
        return image(qrCodeService.generateFromText(text));
    }

    @PostMapping("/contact")
    public ResponseEntity<byte[]> fromContact(@RequestBody QrCodeServiceImpl.ContactRequest contact) throws Exception {
        return image(qrCodeService.generateFromContact(contact));
    }

    private ResponseEntity<byte[]> image(byte[] png) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qr.png\"")
                .body(png);
    }
}