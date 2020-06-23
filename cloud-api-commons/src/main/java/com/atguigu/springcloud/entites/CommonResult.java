package com.atguigu.springcloud.entites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: cloud2020
 * @description:
 *  1.这里是泛型，是为了让其通用，并不是为某一个类而生的。
 * @author: PP Zhang
 * @create: 2020-06-09 08:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    // 404 not_found
    // code 是约定的错误编码。
    private  Integer code;
    // message是错误信息
    private String message;
    // 指定类型的数据
    private  T data;
    // 两参的构造函数
    public  CommonResult(Integer code,String message){
        this(code,message,null);
    }
}
