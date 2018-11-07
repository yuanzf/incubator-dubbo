package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.rpc.*;

/**
 * @Author: yzf
 * @Date: 2018/11/7 10:57
 * @Desoription:
 */
public class TestFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        System.out.println("Filter方法之前测试");
        System.out.println(System.currentTimeMillis());
        Result invoke = invoker.invoke(invocation);
        System.out.println("Filter方法之后测试");
        System.out.println(System.currentTimeMillis());
        return invoke;
    }
}
