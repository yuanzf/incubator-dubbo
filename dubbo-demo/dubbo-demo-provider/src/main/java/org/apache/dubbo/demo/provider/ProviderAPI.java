package org.apache.dubbo.demo.provider;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.demo.DemoService;

import java.io.IOException;

/**
 * @Author: yzf
 * @Date: 2018/10/24 17:10
 * @Desoription:
 */
public class ProviderAPI {
    public static void main(String[] args) {
        DemoServiceImpl demoService = new DemoServiceImpl();

        //当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("providerAPI");

        //连接注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("multicast://224.5.6.7:1234");


        //服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setPort(20880);
        protocolConfig.setName("dubbo");
        protocolConfig.setThreads(20);

        ServiceConfig<DemoServiceImpl> demoServiceServiceConfig = new ServiceConfig<>();
        demoServiceServiceConfig.setApplication(applicationConfig);
        demoServiceServiceConfig.setRegistry(registryConfig);
        demoServiceServiceConfig.setProtocol(protocolConfig);
        demoServiceServiceConfig.setInterface(DemoService.class);
        demoServiceServiceConfig.setRef(demoService);
        demoServiceServiceConfig.setVersion("1.0.0");
        demoServiceServiceConfig.setGroup("test");

        demoServiceServiceConfig.export();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
