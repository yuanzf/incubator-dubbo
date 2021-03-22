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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * random load balance.
 * 默认使用该算法
 * 按权重设置随机概率，在一个节点上碰撞的概率高，但调用量越大分布越均匀，而且按
 * 概率使用权重后也比较均匀，有利于动态调整提供者的权重
 *
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    public static final String NAME = "random";

    @Override
    protected <T> Invoker<T> doSelect(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        // Number of invokers，获取invoker的数量
        int length = invokers.size();
        // The sum of weights 记录总的权重
        int totalWeight = 0;
        // Every invoker has the same weight?  每个Invoker的权重是否相同
        boolean sameWeight = true;
        for (int i = 0; i < length; i++) {
            //获取Invoker的权重
            int weight = getWeight(invokers.get(i), invocation);
            //对权重求和
            totalWeight += weight;
            if (sameWeight && i > 0
                    && weight != getWeight(invokers.get(i - 1), invocation)) {
                //判断当前Invoker和前一个invoker是否相同，如果不相同则sameWeight=false。
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            //如果每个Invoker的每个权重不相同，则走此逻辑.随机产生一个值，如果通过该值计算使用哪个Invoker,此处使用了ThreadLocalRandom，性能更好
            // If (not every invoker has the same weight & at least one invoker's weight>0), select randomly based on totalWeight.
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            // Return a invoker based on the random value.
            for (int i = 0; i < length; i++) {
                //遍历所有的Invoker，累减，得到被选中的Invoker,
                offset -= getWeight(invokers.get(i), invocation);
                if (offset < 0) {
                    return invokers.get(i);
                }
            }
        }
        // If all invokers have the same weight value or totalWeight=0, return evenly.
        return invokers.get(ThreadLocalRandom.current().nextInt(length));
    }

}
