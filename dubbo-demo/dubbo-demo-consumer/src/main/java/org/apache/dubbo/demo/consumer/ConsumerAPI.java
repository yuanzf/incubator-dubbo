package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.demo.DemoService;

/**
 * @Author: yzf
 * @Date: 2018/9/29 15:05
 * @Desoription:
 */
public class ConsumerAPI {
    public static void main(String[] args) {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("demo_consumer_API");

        //连接注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("224.5.6.7:1234");
        registryConfig.setProtocol("multicast");

        //引用用远程服务
        ReferenceConfig<DemoService> demoServiceReferenceConfig = new ReferenceConfig<>();
        demoServiceReferenceConfig.setApplication(applicationConfig);
        demoServiceReferenceConfig.setRegistry(registryConfig);
        demoServiceReferenceConfig.setInterface(DemoService.class);

        DemoService demoService = demoServiceReferenceConfig.get();

        while (true) {
            try {
                Thread.sleep(1000);
                String hello = demoService.sayHello("hello");
                System.out.println(hello);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
