package com.atguigu.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * @program: cloud2020
 * @description:
 * @author: PP Zhang
 * @create: 2020-06-17 11:05
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService {
    @Override
    public String paymentInfo_OK(Integer id) {
        return  "----------PaymentFallbackService fall back-paymentInfo_OK o(∩_∩)o 哈哈";
    }

    @Override
    public String paymentInfo_TimeOut(Integer id) {
        return "----------PaymentFallbackService fall back-paymentInfo_TimeOut,~~~~(>_<)~~~~";
    }
}
