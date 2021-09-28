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

package com.zfoo.protocol.serializer.js;

import com.zfoo.protocol.generate.GenerateProtocolFile;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import com.zfoo.protocol.util.StringUtils;

import java.lang.reflect.Field;

import static com.zfoo.protocol.util.FileUtils.LS;


/**
 * @author islandempty
 */
public class JsShortSerializer implements IJsSerializer {

    @Override
    public void writeObject(StringBuilder builder, String objectStr, int deep, Field field, IFieldRegistration fieldRegistration) {
        GenerateProtocolFile.addTab(builder, deep);
        builder.append(StringUtils.format("byteBuffer.writeShort({});", objectStr)).append(LS);
    }

    @Override
    public String readObject(StringBuilder builder, int deep, Field field, IFieldRegistration fieldRegistration) {
        String result = "result" + GenerateProtocolFile.index.getAndIncrement();

        GenerateProtocolFile.addTab(builder, deep);
        builder.append(StringUtils.format("const {} = byteBuffer.readShort();", result)).append(LS);
        return result;
    }
}
