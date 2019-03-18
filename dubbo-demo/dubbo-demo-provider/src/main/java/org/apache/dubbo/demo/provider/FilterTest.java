package org.apache.dubbo.demo.provider;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.rpc.*;

/**
 * @Author: yzf
 * @Date: 2019-03-04 17:27
 * @Desoription:
 */
public class FilterTest implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result invoke = invoker.invoke(invocation);
        System.out.println("filter1 执行");
        return invoke;
    }
}
