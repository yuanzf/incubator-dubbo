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
        //设置远程服务的依赖接口及依赖接口的名称(ReferenceConfig.interfaceClass和ReferenceName的值)
        demoServiceReferenceConfig.setInterface(DemoService.class);

        while (true) {
            try {
                //获取invoker,如果有则直接返回，如果没有则进行初始化(ReferenceConfig.init)
                DemoService demoService = demoServiceReferenceConfig.get();
                Thread.sleep(1000);
                String hello = demoService.sayHello("hello");
                System.out.println(hello);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
