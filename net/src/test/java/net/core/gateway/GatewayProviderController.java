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

package net.core.gateway;

import com.zfoo.net.NetContext;
import com.zfoo.net.dispatcher.model.anno.PacketReceiver;

import com.zfoo.net.packet.model.GatewayPacketAttachment;
import com.zfoo.net.session.model.Session;
import com.zfoo.protocol.util.JsonUtils;
import com.zfoo.protocol.util.StringUtils;
import net.packet.gateway.GatewayToProviderRequest;
import net.packet.gateway.GatewayToProviderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author jaysunxiao
 * @version 3.0
 */
@Component
public class GatewayProviderController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayProviderController.class);

    @PacketReceiver
    public void atGatewayToProviderRequest(Session session, GatewayToProviderRequest request, GatewayPacketAttachment gatewayAttachment) {
        logger.info("provider receive [packet:{}] from client", JsonUtils.object2String(request));

        var response = new GatewayToProviderResponse();
        response.setMessage(StringUtils.format("Hello, this is the [provider:{}] response!", NetContext.getConfigManager().getLocalConfig().toLocalRegisterVO().toString()));

        NetContext.getDispatcher().send(session, response, gatewayAttachment);
    }
}
