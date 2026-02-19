package com.yash.qrgenerator.service.impl;

import com.yash.qrgenerator.service.QrCodeService;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeServiceImpl implements QrCodeService {


    private static final int DEFAULT_SIZE = 300;

    // ── URL ──────────────────────────────────────────────────────────────────
    public byte[] generateFromUrl(String url) throws Exception {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        return generate(url);
    }

    // ── Plain Text ────────────────────────────────────────────────────────────
    public byte[] generateFromText(String text) throws Exception {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text must not be empty");
        }
        return generate(text);
    }

    // ── vCard (Contact) ───────────────────────────────────────────────────────
    public byte[] generateFromContact(ContactRequest contact) throws Exception {
        String vcard = buildVCard(contact);
        return generate(vcard);
    }

    // ── Core generation logic ─────────────────────────────────────────────────
    private byte[] generate(String content) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        BitMatrix matrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, DEFAULT_SIZE, DEFAULT_SIZE, hints);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }

    // ── vCard builder ─────────────────────────────────────────────────────────
    private String buildVCard(ContactRequest c) {
        return "BEGIN:VCARD\n"
                + "VERSION:3.0\n"
                + "FN:" + nvl(c.firstName()) + " " + nvl(c.lastName()) + "\n"
                + "N:" + nvl(c.lastName()) + ";" + nvl(c.firstName()) + ";;;\n"
                + "ORG:" + nvl(c.organization()) + "\n"
                + "TEL:" + nvl(c.phone()) + "\n"
                + "EMAIL:" + nvl(c.email()) + "\n"
                + "URL:" + nvl(c.url()) + "\n"
                + "END:VCARD";
    }

    private String nvl(String s) {
        return s != null ? s : "";
    }

    // ── DTO ───────────────────────────────────────────────────────────────────
    public record ContactRequest(
            String firstName,
            String lastName,
            String phone,
            String email,
            String organization,
            String url
    ) {
    }
}