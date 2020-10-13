package com.atguigu.guli.service.sms.service;

import com.netflix.client.ClientException;

public interface SmsService {

    void sendSms(String mobile, String checkCode) throws ClientException;
}
