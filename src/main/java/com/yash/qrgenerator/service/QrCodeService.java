package com.yash.qrgenerator.service;

import com.yash.qrgenerator.service.impl.QrCodeServiceImpl;

public interface QrCodeService {

    byte[] generateFromUrl(String url) throws Exception;

    byte[] generateFromContact(QrCodeServiceImpl.ContactRequest contact) throws Exception;

    byte[] generateFromText(String text) throws Exception;
}
