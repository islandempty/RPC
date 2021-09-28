/*
 * Copyright (C) 2020 The zfoo Authors
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package net.core.udp.server;

import com.zfoo.net.NetContext;
import com.zfoo.net.dispatcher.model.anno.PacketReceiver;
import com.zfoo.net.packet.model.UdpPacketAttachment;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.util.JsonUtils;
import net.packet.udp.UdpHelloRequest;
import net.packet.udp.UdpHelloResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@Component
public class UdpServerPacketController {

    private static final Logger logger = LoggerFactory.getLogger(UdpServerPacketController.class);

    @PacketReceiver
    public void atUdpHelloRequest(Session session, UdpHelloRequest request, UdpPacketAttachment attachment) {
        logger.info("udp server receive [packet:{}] from client", JsonUtils.object2String(request));

        var response = new UdpHelloResponse();
        response.setMessage("Hello, this is the udp server!");

        NetContext.getDispatcher().send(session, response, attachment);
    }

}
