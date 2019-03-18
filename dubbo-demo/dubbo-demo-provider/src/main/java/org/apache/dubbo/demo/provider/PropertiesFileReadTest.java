package org.apache.dubbo.demo.provider;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.utils.ConfigUtils;

import java.util.Properties;

/**
 * @Author: yzf
 * @Date: 2018/10/30 14:51
 * @Desoription:
 */
public class PropertiesFileReadTest {
    public static void main(String[] args) {
        Properties properties = ConfigUtils.getProperties();
        System.out.println(properties);

    }
}
