package com.atguigu.springcloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @program: cloud2020
 * @description:
 * @author: PP Zhang
 * @create: 2020-06-11 22:11
 */
@RestController
@Slf4j
public class PaymentController {
    @Value("${server.port}")
    private  String serverPort;
    @RequestMapping(value = "/payment/zk")
    public  String paymentzk(){
        return "springcloud with zookeeper:"+serverPort + "\t" + UUID.randomUUID().toString();
    }
}
