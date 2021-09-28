/*
 * Copyright (C) 2020 The zfoo Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package net.core.provider;

import com.zfoo.net.NetContext;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.util.ThreadUtils;
import net.packet.provider.ProviderMessAnswer;
import net.packet.provider.ProviderMessAsk;
import net.session.SessionUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@Ignore
public class ProviderTest {

    private static final Logger logger = LoggerFactory.getLogger(ProviderTest.class);

    /**
     * RPC教程：
     * 1.首先必须保证启动zookeeper
     * 2.启动服务提供者，startProvider0，startProvider1，startProvider2
     * 3.启动服务消费者，startSyncRandomConsumer，startAsyncRandomConsumer，startConsistentSessionConsumer，startShortestTimeConsumer
     * 4.每个消费者都是通过不同的策略消费，注意区别
     */
    @Test
    public void startProvider0() {
        var context = new ClassPathXmlApplicationContext("provider/provider_config.xml");
        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    @Test
    public void startProvider1() {
        var context = new ClassPathXmlApplicationContext("provider/provider_config.xml");
        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    @Test
    public void startProvider2() {
        var context = new ClassPathXmlApplicationContext("provider/provider_config.xml");
        SessionUtils.printSessionInfo();
        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    /**
     * 随机消费，同步请求的方式
     */
    @Test
    public void startSyncRandomConsumer() throws Exception {
        var context = new ClassPathXmlApplicationContext("provider/consumer_random_config.xml");
        SessionUtils.printSessionInfo();

        var ask = new ProviderMessAsk();
        ask.setMessage("Hello, this is the consumer!");
        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(3000);
            var response = NetContext.getConsumer().syncAsk(ask, ProviderMessAnswer.class, null).packet();
            logger.info("消费者请求[{}]收到消息[{}]", i, JsonUtils.object2String(response));
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    /**
     * 随机消费，异步请求的方式
     */
    @Test
    public void startAsyncRandomConsumer() {
        var context = new ClassPathXmlApplicationContext("provider/consumer_random_config.xml");
        SessionUtils.printSessionInfo();

        var ask = new ProviderMessAsk();
        ask.setMessage("Hello, this is the consumer!");
        var atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(3000);
            NetContext.getConsumer().asyncAsk(ask, ProviderMessAnswer.class, null).whenComplete(answer -> {
                logger.info("消费者请求[{}]收到消息[{}]", atomicInteger.incrementAndGet(), JsonUtils.object2String(answer));
            });
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    /**
     * 一致性hash算法消费方式
     */
    @Test
    public void startConsistentSessionConsumer() {
        var context = new ClassPathXmlApplicationContext("provider/consumer_consistent_session_config.xml");
        SessionUtils.printSessionInfo();

        var ask = new ProviderMessAsk();
        ask.setMessage("Hello, this is the consumer!");
        var atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(3000);
            NetContext.getConsumer().asyncAsk(ask, ProviderMessAnswer.class, 100).whenComplete(answer -> {
                logger.info("消费者请求[{}]收到消息[{}]", atomicInteger.incrementAndGet(), JsonUtils.object2String(answer));
            });
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }

    /**
     * 最短时间的消费方式
     */
    @Test
    public void startShortestTimeConsumer() {
        var context = new ClassPathXmlApplicationContext("provider/consumer_shortest_time_config.xml");
        SessionUtils.printSessionInfo();

        var ask = new ProviderMessAsk();
        ask.setMessage("Hello, this is the consumer!");
        var atomicInteger = new AtomicInteger(0);

        for (int i = 0; i < 1000; i++) {
            ThreadUtils.sleep(3000);
            NetContext.getConsumer().asyncAsk(ask, ProviderMessAnswer.class, null).whenComplete(answer -> {
                logger.info("消费者请求[{}]收到消息[{}]", atomicInteger.incrementAndGet(), JsonUtils.object2String(answer));
            });
        }

        ThreadUtils.sleep(Long.MAX_VALUE);
    }


}
