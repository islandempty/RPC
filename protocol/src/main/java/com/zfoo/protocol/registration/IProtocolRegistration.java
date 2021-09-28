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

package com.zfoo.protocol.registration;

import com.zfoo.protocol.IPacket;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

/**
 * @author islandempty
 */
public interface IProtocolRegistration {

    short protocolId();

    byte module();

    Constructor<?> protocolConstructor();

    /**
     * 协议接收器，回调方法，主要是存放一些额外的参数
     */
    Object receiver();

    /**
     * 序列化
     */
    void write(ByteBuf buffer, IPacket packet);

    /**
     * 反序列化
     */
    Object read(ByteBuf buffer);

}
