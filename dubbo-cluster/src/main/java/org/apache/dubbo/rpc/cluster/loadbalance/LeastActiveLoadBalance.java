/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc.cluster.loadbalance;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcStatus;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LeastActiveLoadBalance
 * 最少活跃调用数字
 */
public class LeastActiveLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "leastactive";

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        //获取Invoker的数量
        int length = invokers.size();
        // 最少活跃调用数
        int leastActive = -1;
        // 最少活跃调用数的个数
        int leastCount = 0;
        // The index of invokers having the same least active value (leastActive)
        int[] leastIndexes = new int[length];
        // 权重总和（相同活跃数的权重总和）
        int totalWeight = 0;
        // 权重初始值
        int firstWeight = 0;
        //所有的Invoker的权重是否相等
        boolean sameWeight = true;
        /**
         * 此处的for循环找到了Invoker中活跃数最少的Invoker，其中Invoker的索引未知保存在leastIndexes中，
         * */
        for (int i = 0; i < length; i++) {
            Invoker<T> invoker = invokers.get(i);
            // 获取活跃数
            int active = RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName()).getActive();
            //获取权重
            int afterWarmup = getWeight(invoker, invocation);
            // Restart, when find a invoker having smaller least active value.
            if (leastActive == -1 || active < leastActive) {
                //记录当前最小活跃数
                leastActive = active;
                // Reset leastCount, count again based on current leastCount
                leastCount = 1;
                //记录最小活跃数的索引
                leastIndexes[0] = i;
                //计算权重和
                totalWeight = afterWarmup;
                // 记录最小活跃数Invoker的权重
                firstWeight = afterWarmup;
                // 是否有相同的权重
                sameWeight = true;
            } else if (active == leastActive) {
                //当前Invoker的活跃数和最小活跃数相等
                //记录Invoker的索引未知，   leastCount 最小活跃数的的个数
                leastIndexes[leastCount++] = i;
                //计算总权重
                totalWeight += afterWarmup;
                // 判断相同活跃数的权重是否相等
                if (sameWeight && i > 0
                        && afterWarmup != firstWeight) {
                    sameWeight = false;
                }
            }
        }
        //最少活跃调用数唯一，直接调用Invoker
        if (leastCount == 1) {
            return invokers.get(leastIndexes[0]);
        }
        //最少活跃数多于一个并且权重不相等，则采用随机访问（RandomLoadBalance算法访问）
        if (!sameWeight && totalWeight > 0) {
            //在权重范围内获取一个随机数
            int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight) + 1;
            // Return a invoker based on the random value.
            for (int i = 0; i < leastCount; i++) {
                int leastIndex = leastIndexes[i];
                offsetWeight -= getWeight(invokers.get(leastIndex), invocation);
                if (offsetWeight <= 0) {
                    return invokers.get(leastIndex);
                }
            }
        }
        // 如果最少活跃数多于一个，并且每个权重都相等，则在小于 最小活跃数的范围内随机产生一个数
        return invokers.get(leastIndexes[ThreadLocalRandom.current().nextInt(leastCount)]);
    }
}
