package com.springcloud.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.springcloud.sentinel.ExceptionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: lkx
 * @email: lkx19950811@163.com
 * @date: create in 14:41 2019/12/16
 */
@RestController
public class HelloController {
    @Value("spring.application.name")
    String appName;
    @SentinelResource("resource")
    @RequestMapping("/sentinel/hello")
    public Map<String,Object> hello(){
        Map<String,Object> map=new HashMap<>(2);
        map.put("appName",appName);
        map.put("method","hello");
        return map;
    }
    /**
     * 通过控制台配置URL 限流
     * @return
     */
    @RequestMapping("/sentinel/test")
    public Map<String,Object> test(){
        Map<String,Object> map=new HashMap<>(2);
        map.put("appName",appName);
        map.put("method","test");
        return map;
    }
    //通过注解限流并自定义限流逻辑
    @SentinelResource(value = "resource2", blockHandler = "handleException", blockHandlerClass = {ExceptionUtil.class})
    @RequestMapping("/sentinel/test2")
    public Map<String,Object> test2() {
        Map<String,Object> map=new HashMap<>();
        map.put("method","test2");
        map.put("msg","自定义限流逻辑处理");
        return  map;
    }
    // 熔断降级
    @SentinelResource(value="hello",fallback="helloFallback")
    @RequestMapping("/hello")
    public String helloworld3() {
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {
        }

        return "hello Sentinel333 !" ;
    }
    // 该方法降级处理函数，参数要与原函数helloworld3相同，并且返回值类型也要与原函数相同，此外，该方法必须与原函数在同一个类中
    public String helloFallback(){
        return "hello Sentinel ! fallback ! 触发熔断方法";
    }
}
