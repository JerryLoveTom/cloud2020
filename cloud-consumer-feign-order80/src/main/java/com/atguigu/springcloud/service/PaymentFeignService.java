package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entites.CommonResult;
import com.atguigu.springcloud.entites.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: cloud2020
 * @description:
 * 下面类的整体意思为：
 *  通过@FeignClient(value = "CLOUD-PAYMENT-SERVICE")中找到消息注册中心中名为CLOUD-PAYMENT-SERVICE的服务
 *  然后调用其下面的@GetMapping(value="/payment/get/{id}")这个方法。
 * @author: PP Zhang
 * @create: 2020-06-15 10:35
 */
@Component // 能被扫描
@FeignClient(value = "CLOUD-PAYMENT-SERVICE") // 通过Feign调用的哪个微服务上的方法。
public interface PaymentFeignService {
    // 直接调用的时8001Controller中的同样的方法
    @GetMapping(value="/payment/get/{id}")
    public CommonResult<Payment>getPaymentById(@PathVariable("id") Long id);

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeOut();
}
