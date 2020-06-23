package com.atguigu.springcloud.service.impl;

import cn.hutool.core.util.IdUtil;
import com.atguigu.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @program: cloud2020
 * @description:
 * @author: PP Zhang
 * @create: 2020-06-15 15:31
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    // ==== 服务降级
    /**
    * @Description: 正常访问
    * @Param:
    * @return:
    * @Author: PP Zhang
    * @Date: 2020/6/15
    */
    @Override
    public String paymentInfo_OK(Integer id) {
        return "线程池" + Thread.currentThread().getName() + "paymentInfo_OK,id "+"\t"+"!!!!";
    }
    // 一旦服务调用方法失败并抛出异常错误信息后，会自动调用@HystrixCommand标注好的fallbackMethod调用类中指定的方法
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000") // 当前方法5秒以内走正常业务逻辑，超时进行兜底
    }) // 意思当前方法出问题（访问超时等），那么由paymentInfo_TimeOutHandler方法进行兜底处理。
    @Override
    public String paymentInfo_TimeOut(Integer id) {
        // int a = 10/0;
        int timeNum = 3;
        try {
            TimeUnit.SECONDS.sleep(timeNum);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池" + Thread.currentThread().getName() + "paymentInfo_TimeOut,id "+"\t"+"!!!! 耗时"+timeNum+"秒钟";
    }
    public String paymentInfo_TimeOutHandler(Integer id){
        return "线程池" + Thread.currentThread().getName() + "paymentInfo_TimeOutHandler,id "+"\t"+id+"!~~~~(>_<)~~~~";
    }


    // ====服务熔断 HystrixCommandProperties通过它可以获取下面参数内容。
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value="10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value="10000"), // 时间范围
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value="60"), // 失败率达到多少后跳闸
    })
    @Override
    public String paymentCircuitBreaker(Integer id){
        if (id < 0){
            // 故意抛异常，走paymentCircuitBreaker_fallback方法
            throw new RuntimeException("******id 不能为负数");
        }
        String serialNum = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功，流水号："+ serialNum;
    }
    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id不能为负数，请稍后再试~~~~(>_<)~~~~   id:"+ id;
    }

}
