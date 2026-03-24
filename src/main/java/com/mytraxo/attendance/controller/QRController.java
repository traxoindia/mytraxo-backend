package com.mytraxo.attendance.controller;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/qr")
public class QRController {

    // ✅ Generate QR token (Admin)
    @GetMapping("/generate")
    public String generateQR() {
        return UUID.randomUUID().toString();
    }
}