package org.apache.dubbo.common;

import org.apache.dubbo.common.utils.StringUtils;

/**
 * @Author: yzf
 * @Date: 2021/2/22 22:54
 * @Desoription:
 */
public class Main {
    public static void main(String[] args) {
        String s="beanName";
        String s1 = StringUtils.camelToSplitName(s, "_");
        System.out.println(s1);
    }
}
