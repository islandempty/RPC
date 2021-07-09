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

package com.zfoo.protocol.serializer.enhance;

import com.zfoo.protocol.generate.GenerateProtocolFile;
import com.zfoo.protocol.registration.EnhanceUtils;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import com.zfoo.protocol.util.StringUtils;

import java.lang.reflect.Field;


public class EnhanceShortSerializer implements IEnhanceSerializer {

    @Override
    public void writeObject(StringBuilder builder, String objectStr, Field field, IFieldRegistration fieldRegistration) {
        if (field.getType().isPrimitive() || (field.getType().isArray() && field.getType().getComponentType().isPrimitive())) {
            builder.append(StringUtils.format("{}.writeShort($1, {});", EnhanceUtils.byteBufUtils, objectStr));
        } else {
            builder.append(StringUtils.format("{}.writeShortBox($1, (Short){});", EnhanceUtils.byteBufUtils, objectStr));
        }
    }

    @Override
    public String readObject(StringBuilder builder, Field field, IFieldRegistration fieldRegistration) {
        var result = "result" + GenerateProtocolFile.index.getAndIncrement();

        if (field.getType().isPrimitive() || (field.getType().isArray() && field.getType().getComponentType().isPrimitive())) {
            builder.append(StringUtils.format("short {} = {}.readShort($1);", result, EnhanceUtils.byteBufUtils));
        } else {
            builder.append(StringUtils.format("Short {} = {}.readShortBox($1);", result, EnhanceUtils.byteBufUtils));
        }
        return result;
    }

}
