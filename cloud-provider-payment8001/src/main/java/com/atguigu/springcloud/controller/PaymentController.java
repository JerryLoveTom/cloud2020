package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entites.CommonResult;
import com.atguigu.springcloud.entites.Payment;
import com.atguigu.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: cloud2020
 * @description:
 * @author: PP Zhang
 * @create: 2020-06-09 10:20
 */
@RestController
@Slf4j
public class PaymentController {
    @Resource
    private PaymentService paymentService;
    @Value("${server.port}")
    private String serverPort;

    @Resource
    private DiscoveryClient discoveryClient; // 通过服务发现来获取服务信息

    @PostMapping(value="/payment/create")
    public CommonResult<Payment>create(@RequestBody  Payment payment){
        int result = paymentService.create(payment);
        log.info("*********插入结果:" + result);
        if (result > 0){
            return  new CommonResult(200,"插入数据成功,serverPort:"+serverPort,result);
        }else {
            return  new CommonResult(444,"插入数据失败",null);
        }
    }
    @GetMapping(value="/payment/get/{id}")
    public CommonResult<Payment>getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("*********查询结果:" + payment);
        if (payment != null){
            return  new CommonResult(200,"查询数据成功：serverPort:"+serverPort,payment);
        }else {
            return  new CommonResult(444,"没有对应记录，查询ID："+id,null);
        }
    }
    @GetMapping(value = "/payment/discovery")
    public  Object discovery(){
        List<String>list = discoveryClient.getServices(); // 获取注册中心所有的服务。
        for (String str:list  ) {
            log.info("*******str:"+str);
        }
        // 通过对外暴露的对外暴露的微服务名称，来获取该服务名称下所有的信息。
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            // 获取服务名称id、主机地址、端口、访问地址等。
            log.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }
        return this.discoveryClient;
    }
    // 把当前接口返回,没有什么意义，就是故意停留3秒
    @GetMapping(value = "/payment/lb")
    public String getPaymentLB(){
        return serverPort;
    }

    // 故意暂定方法,测试Feign超时。
    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeOut(){
      try {
          TimeUnit.SECONDS.sleep(3);
      }catch (InterruptedException e){
          e.printStackTrace();
      }
      return serverPort;
    }

}
