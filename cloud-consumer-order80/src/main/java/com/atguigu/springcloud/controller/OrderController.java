package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entites.CommonResult;
import com.atguigu.springcloud.entites.Payment;
import com.atguigu.springcloud.lb.LoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

/**
 * @program: cloud2020
 * @description:
 * @author: PP Zhang
 * @create: 2020-06-09 15:17
 */
@RestController
@Slf4j
public class OrderController {
    // 服务提供者的访问地址
    // public static  final String PAYMENT_URL = "http://localhost:8001";

    public static  final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";

    // restTemplate 是给httpclinet做了一次封装
    // 什么是 restTemplate？提供了多种便捷访问远程HTTP服务方法，是一种便捷访问restful服务模板类，是spring提供的用于访问rest服务的客户端模版工具类
    /*
    * 使用：
    *   1.使用restTemplate访问restful接口非常简单粗暴（URL，requestMap，ResponseBean.class）这三个参数分别代表
    * REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
    *
    * */

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalancer loadBalancer;

    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment>create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create",payment,CommonResult.class);
    }
    @GetMapping("/consumer/payment/createEntity")
    public CommonResult<Payment>create2(Payment payment){
        ResponseEntity<CommonResult> entity =  restTemplate.postForEntity(PAYMENT_URL + "/payment/create",payment,CommonResult.class);
        if (entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else {
            return new CommonResult<>(222,"操作失败");
        }
    }
    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment>getPayment(@PathVariable("id") Long id){
        return restTemplate.getForObject(PAYMENT_URL + "/payment/get/"+id,CommonResult.class);
    }

    @GetMapping("/consumer/payment/getForEntity/{id}")
    public  CommonResult<Payment> getPayment2(@PathVariable("id") Long id){
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/"+id,CommonResult.class);
        if (entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }else {
            return  new CommonResult<>(444,"操作失败");
        }
    }

    @GetMapping(value = "/consumer/payment/lb")
    public String getPaymentLB(){
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        if (instances == null || instances.size() <= 0){
            return null;
        }
        ServiceInstance serviceInstance = loadBalancer.instances(instances);
        URI uri = serviceInstance.getUri();
        return restTemplate.getForObject(uri+"/payment/lb",String.class);
    }
}
