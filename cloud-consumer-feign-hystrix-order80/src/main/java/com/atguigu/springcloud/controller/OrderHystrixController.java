package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entites.CommonResult;
import com.atguigu.springcloud.entites.Payment;
import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: cloud2020
 * @description:
 * @author: PP Zhang
 * @create: 2020-06-16 21:24
 */
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_Fallback") // 统一跳转到统一的处理页面。
public class OrderHystrixController {
    @Resource
    private PaymentHystrixService paymentHystrixService;

    @GetMapping(value = "/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id){
        String result = paymentHystrixService.paymentInfo_OK(id);
        return  result;
    }
    @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "1500") // 当前方法1.5秒以内走正常业务逻辑，超时进行兜底
    }) // 意思当前方法出问题（访问超时等），那么由paymentInfo_TimeOutHandler方法进行兜底处理。
    @GetMapping(value = "/consumer/payment/hystrix/timeout/{id}")
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id){
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        return result;
    }
    @HystrixCommand // 没有特别指明异常处理方法，那么就是用全局@DefaultProperties 指定的错误异常处理方法。
    @GetMapping(value = "/consumer/payment/hystrix/timeout2/{id}")
    public String paymentInfo_TimeOut2(@PathVariable("id") Integer id){
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        return result;
    }
    // 独享的错误、异常处理
    public String paymentTimeOutFallbackMethod(@PathVariable("id") Integer id){
        return "我是消费者80，对方支付系统繁忙请等待10秒钟后再试或者自己运行错误，请检查自己~~~~(>_<)~~~~";
    }
    // 全局的错误、异常处理
    public String payment_Global_Fallback(){
        return "我是消费者80，全局异常处理信息，请检查自己~~~~(>_<)~~~~";
    }
}
