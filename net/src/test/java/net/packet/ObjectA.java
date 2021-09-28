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

package net.packet;

import com.zfoo.protocol.IPacket;

import java.util.Objects;

/**
 * @author jaysunxiao
 * @version 3.0
 */
public class ObjectA implements IPacket {

    public static final transient short PROTOCOL_ID = 1116;

    private int a;


    private ObjectB objectB;

    @Override
    public short protocolId() {
        return PROTOCOL_ID;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }


    public ObjectB getObjectB() {
        return objectB;
    }

    public void setObjectB(ObjectB objectB) {
        this.objectB = objectB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectA objectA = (ObjectA) o;
        return a == objectA.a &&
                Objects.equals(objectB, objectA.objectB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, objectB);
    }
}
