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

package com.zfoo.protocol.packet;

import com.zfoo.protocol.IPacket;

public class SimpleObject implements IPacket {

    public static final transient short PROTOCOL_ID = 1163;

    private int c;

    private boolean g;

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public boolean isG() {
        return g;
    }

    public void setG(boolean g) {
        this.g = g;
    }
}
