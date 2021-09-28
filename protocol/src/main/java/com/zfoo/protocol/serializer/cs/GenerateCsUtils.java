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

package com.zfoo.protocol.serializer.cs;

import com.zfoo.protocol.generate.GenerateProtocolDocument;
import com.zfoo.protocol.generate.GenerateProtocolFile;
import com.zfoo.protocol.generate.GenerateProtocolPath;
import com.zfoo.protocol.model.Pair;
import com.zfoo.protocol.registration.ProtocolRegistration;
import com.zfoo.protocol.registration.field.IFieldRegistration;
import com.zfoo.protocol.serializer.*;
import com.zfoo.protocol.util.ClassUtils;
import com.zfoo.protocol.util.FileUtils;
import com.zfoo.protocol.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.zfoo.protocol.util.FileUtils.LS;
import static com.zfoo.protocol.util.StringUtils.TAB;

/**
 * @author islandempty
 */
public abstract class GenerateCsUtils {

    private static final String PROTOCOL_OUTPUT_ROOT_PATH = "CsProtocol/";

    private static Map<ISerializer, ICsSerializer> csSerializerMap;

    public static ICsSerializer csSerializer(ISerializer serializer) {
        return csSerializerMap.get(serializer);
    }

    public static void init() {
        FileUtils.deleteFile(new File(PROTOCOL_OUTPUT_ROOT_PATH));
        FileUtils.createDirectory(PROTOCOL_OUTPUT_ROOT_PATH);

        csSerializerMap = new HashMap<>();
        csSerializerMap.put(BooleanSerializer.getInstance(), new CsBooleanSerializer());
        csSerializerMap.put(ByteSerializer.getInstance(), new CsByteSerializer());
        csSerializerMap.put(ShortSerializer.getInstance(), new CsShortSerializer());
        csSerializerMap.put(IntSerializer.getInstance(), new CsIntSerializer());
        csSerializerMap.put(LongSerializer.getInstance(), new CsLongSerializer());
        csSerializerMap.put(FloatSerializer.getInstance(), new CsFloatSerializer());
        csSerializerMap.put(DoubleSerializer.getInstance(), new CsDoubleSerializer());
        csSerializerMap.put(CharSerializer.getInstance(), new CsCharSerializer());
        csSerializerMap.put(StringSerializer.getInstance(), new CsStringSerializer());
        csSerializerMap.put(ArraySerializer.getInstance(), new CsArraySerializer());
        csSerializerMap.put(ListSerializer.getInstance(), new CsListSerializer());
        csSerializerMap.put(SetSerializer.getInstance(), new CsSetSerializer());
        csSerializerMap.put(MapSerializer.getInstance(), new CsMapSerializer());
        csSerializerMap.put(ObjectProtocolSerializer.getInstance(), new CsObjectProtocolSerializer());
    }

    public static void clear() {
        csSerializerMap = null;
    }

    /**
     * 生成协议依赖的工具类
     */
    public static void createProtocolManager() throws IOException {
        var list = List.of("cs/ProtocolManager.cs"
                , "cs/IProtocolRegistration.cs"
                , "cs/IPacket.cs"
                , "cs/Buffer/ByteBuffer.cs"
                , "cs/Buffer/LittleEndianByteBuffer.cs"
                , "cs/Buffer/BigEndianByteBuffer.cs");

        for (var fileName : list) {
            var fileInputStream = ClassUtils.getFileFromClassPath(fileName);
            var createFile = new File(StringUtils.format("{}{}", PROTOCOL_OUTPUT_ROOT_PATH, StringUtils.substringAfterFirst(fileName, "cs/")));
            FileUtils.writeInputStreamToFile(createFile, fileInputStream);
        }
    }

    /**
     * 生成协议类
     */
    public static void createCsProtocolFile(ProtocolRegistration registration) {
        GenerateProtocolFile.index.set(0);

        var protocolId = registration.protocolId();
        var registrationConstructor = registration.getConstructor();
        IFieldRegistration[] fieldRegistrations = registration.getFieldRegistrations();

        var protocolClazzName = registrationConstructor.getDeclaringClass().getSimpleName();

        var csBuilder = new StringBuilder();
        csBuilder.append("using System;").append(LS);
        csBuilder.append("using System.Collections.Generic;").append(LS);
        csBuilder.append("using CsProtocol.Buffer;").append(LS).append(LS);
        csBuilder.append("namespace CsProtocol").append(LS);
        csBuilder.append("{").append(LS);


        // protocol object
        csBuilder.append(protocolClass(registration));

        csBuilder.append(TAB).append(StringUtils.format("public class {}Registration : IProtocolRegistration", protocolClazzName)).append(LS);
        csBuilder.append(TAB).append("{").append(LS);

        // ProtocolId method
        csBuilder.append(packetProtocolId(registration));

        // writeObject method
        csBuilder.append(writeObject(registration));

        // readObject method
        csBuilder.append(readObject(registration));

        csBuilder.append(TAB).append("}").append(LS);
        csBuilder.append("}").append(LS);

        var protocolOutputPath = StringUtils.format("{}{}/{}.cs"
                , PROTOCOL_OUTPUT_ROOT_PATH
                , GenerateProtocolPath.getCapitalizeProtocolPath(protocolId)
                , protocolClazzName);
        FileUtils.writeStringToFile(new File(protocolOutputPath), csBuilder.toString());
    }


    public static String toCsClassName(String typeName) {
        typeName = typeName.replaceAll("java.util.|java.lang.", StringUtils.EMPTY);
        typeName = typeName.replaceAll("com\\.[a-zA-Z0-9_.]*\\.", StringUtils.EMPTY);

        // CSharp不适用基础类型的泛型，会影响性能
        switch (typeName) {
            case "boolean":
            case "Boolean":
                typeName = "bool";
                return typeName;
            case "Byte":
                typeName = "byte";
                return typeName;
            case "Short":
                typeName = "short";
                return typeName;
            case "Integer":
                typeName = "int";
                return typeName;
            case "Long":
                typeName = "long";
                return typeName;
            case "Float":
                typeName = "float";
                return typeName;
            case "Double":
                typeName = "double";
                return typeName;
            case "Character":
                typeName = "char";
                return typeName;
            case "String":
                typeName = "string";
                return typeName;
            default:
        }

        // 将boolean转为bool
        typeName = typeName.replace(" boolean ", " bool ");
        typeName = typeName.replace(" Boolean ", " bool ");
        typeName = typeName.replaceAll("[B|b]oolean\\[", "bool[");
        typeName = typeName.replace("<Boolean", "<bool");
        typeName = typeName.replace("Boolean>", "bool>");

        // 将Byte转为byte
        typeName = typeName.replace(" Byte ", " byte ");
        typeName = typeName.replace("Byte[", "byte[");
        typeName = typeName.replace("Byte>", "byte>");
        typeName = typeName.replace("<Byte", "<byte");

        // 将Short转为short
        typeName = typeName.replace(" Short ", " short ");
        typeName = typeName.replace("Short[", "short[");
        typeName = typeName.replace("Short>", "short>");
        typeName = typeName.replace("<Short", "<short");

        // 将Integer转为int
        typeName = typeName.replace(" Integer ", " int ");
        typeName = typeName.replace("Integer[", "int[");
        typeName = typeName.replace("Integer>", "int>");
        typeName = typeName.replace("<Integer", "<int");


        // 将Long转为long
        typeName = typeName.replace(" Long ", " long ");
        typeName = typeName.replace("Long[", "long[");
        typeName = typeName.replace("Long>", "long>");
        typeName = typeName.replace("<Long", "<long");

        // 将Float转为float
        typeName = typeName.replace(" Float ", " float ");
        typeName = typeName.replace("Float[", "float[");
        typeName = typeName.replace("Float>", "float>");
        typeName = typeName.replace("<Float", "<float");

        // 将Double转为double
        typeName = typeName.replace(" Double ", " double ");
        typeName = typeName.replace("Double[", "double[");
        typeName = typeName.replace("Double>", "double>");
        typeName = typeName.replace("<Double", "<double");

        // 将Character转为Char
        typeName = typeName.replace(" Character ", " char ");
        typeName = typeName.replace("Character[", "char[");
        typeName = typeName.replace("Character>", "char>");
        typeName = typeName.replace("<Character", "<char");

        // 将String转为string
        typeName = typeName.replace("String ", "string ");
        typeName = typeName.replace("String[", "string[");
        typeName = typeName.replace("String>", "string>");
        typeName = typeName.replace("<String", "<string");

        // 将Map转为Dictionary
        typeName = typeName.replace("Map<", "Dictionary<");

        // 将Set转为HashSet
        typeName = typeName.replace("Set<", "HashSet<");

        // 将private转为public
        typeName = typeName.replace(" private ", " public ");

        return typeName;
    }

    private static String protocolClass(ProtocolRegistration registration) {
        short protocolId = registration.getId();
        Field[] fields = registration.getFields();
        var protocolClazzName = registration.getConstructor().getDeclaringClass().getSimpleName();

        var protocolDocument = GenerateProtocolDocument.getProtocolDocument(protocolId);
        var docTitle = protocolDocument.getKey();
        var docFieldMap = protocolDocument.getValue();

        var csBuilder = new StringBuilder();
        if (!StringUtils.isBlank(docTitle)) {
            Arrays.stream(docTitle.split(LS)).forEach(it -> csBuilder.append(TAB).append(it).append(LS));
        }
        csBuilder.append(TAB)
                .append(StringUtils.format("public class {} : IPacket", protocolClazzName))
                .append(LS);
        csBuilder.append(TAB).append("{").append(LS);

        // 协议的属性生成
        var filedList = new ArrayList<Pair<String, String>>();
        for (var field : fields) {
            var propertyType = toCsClassName(field.getGenericType().getTypeName());
            var propertyName = field.getName();

            var propertyFullName = StringUtils.format("public {} {};", propertyType, propertyName);
            // 生成注释
            var doc = docFieldMap.get(propertyName);
            if (!StringUtils.isBlank(doc)) {
                Arrays.stream(doc.split(LS)).forEach(it -> csBuilder.append(TAB + TAB).append(it).append(LS));
            }

            csBuilder.append(TAB + TAB).append(propertyFullName).append(LS);
            filedList.add(new Pair<>(propertyType, propertyName));
        }

        csBuilder.append(LS);

        // ValueOf()方法
        var valueOfParams = filedList.stream()
                .map(it -> StringUtils.format("{} {}", it.getKey(), it.getValue()))
                .collect(Collectors.toList());

        csBuilder.append(TAB + TAB)
                .append(StringUtils.format("public static {} ValueOf({})", protocolClazzName, StringUtils.joinWith(StringUtils.COMMA + " ", valueOfParams.toArray())))
                .append(LS);

        csBuilder.append(TAB + TAB).append("{").append(LS);
        csBuilder.append(TAB + TAB + TAB)
                .append(StringUtils.format("var packet = new {}();", protocolClazzName))
                .append(LS);
        filedList.forEach(it -> csBuilder.append(TAB + TAB + TAB).append(StringUtils.format("packet.{} = {};", it.getValue(), it.getValue())).append(LS));
        csBuilder.append(TAB + TAB + TAB).append("return packet;").append(LS);
        csBuilder.append(TAB + TAB).append("}").append(LS);
        csBuilder.append(LS).append(LS);

        // ProtocolId()方法
        csBuilder.append(TAB + TAB).append("public short ProtocolId()").append(LS);
        csBuilder.append(TAB + TAB).append("{").append(LS);
        csBuilder.append(TAB + TAB + TAB).append(StringUtils.format("return {};", registration.protocolId())).append(LS);
        csBuilder.append(TAB + TAB).append("}").append(LS);
        csBuilder.append(TAB).append("}").append(LS);
        csBuilder.append(LS).append(LS);

        return csBuilder.toString();
    }

    private static String packetProtocolId(ProtocolRegistration registration) {
        var csBuilder = new StringBuilder();
        csBuilder.append(TAB + TAB).append("public short ProtocolId()").append(LS);
        csBuilder.append(TAB + TAB).append("{").append(LS);
        csBuilder.append(TAB + TAB + TAB).append(StringUtils.format("return {};", registration.protocolId())).append(LS);
        csBuilder.append(TAB + TAB).append("}");
        csBuilder.append(LS).append(LS);
        return csBuilder.toString();
    }

    private static String writeObject(ProtocolRegistration registration) {
        Field[] fields = registration.getFields();
        IFieldRegistration[] fieldRegistrations = registration.getFieldRegistrations();
        var protocolClazzName = registration.getConstructor().getDeclaringClass().getSimpleName();

        var csBuilder = new StringBuilder();
        csBuilder.append(TAB + TAB).append("public void Write(ByteBuffer buffer, IPacket packet)").append(LS);
        csBuilder.append(TAB + TAB).append("{").append(LS);
        csBuilder.append(TAB + TAB + TAB).append("if (packet == null)").append(LS);
        csBuilder.append(TAB + TAB + TAB).append("{").append(LS);
        csBuilder.append(TAB + TAB + TAB + TAB).append("buffer.WriteBool(false);").append(LS);
        csBuilder.append(TAB + TAB + TAB + TAB).append("return;").append(LS);
        csBuilder.append(TAB + TAB + TAB + "}").append(LS);
        csBuilder.append(TAB + TAB + TAB).append("buffer.WriteBool(true);").append(LS);

        csBuilder.append(TAB + TAB + TAB)
                .append(StringUtils.format("{} message = ({}) packet;", protocolClazzName, protocolClazzName))
                .append(LS);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            IFieldRegistration fieldRegistration = fieldRegistrations[i];

            csSerializer(fieldRegistration.serializer()).writeObject(csBuilder, "message." + field.getName(), 3, field, fieldRegistration);
        }

        csBuilder.append(TAB + TAB + "}").append(LS).append(LS);
        return csBuilder.toString();
    }


    private static String readObject(ProtocolRegistration registration) {
        Field[] fields = registration.getFields();
        IFieldRegistration[] fieldRegistrations = registration.getFieldRegistrations();
        var protocolClazzName = registration.getConstructor().getDeclaringClass().getSimpleName();

        var csBuilder = new StringBuilder();
        csBuilder.append(TAB + TAB).append("public IPacket Read(ByteBuffer buffer)").append(LS);
        csBuilder.append(TAB + TAB).append("{").append(LS);
        csBuilder.append(TAB + TAB + TAB).append("if (!buffer.ReadBool())").append(LS);
        csBuilder.append(TAB + TAB + TAB).append("{").append(LS);
        csBuilder.append(TAB + TAB + TAB + TAB).append("return null;").append(LS);
        csBuilder.append(TAB + TAB + TAB).append("}").append(LS);

        csBuilder.append(TAB + TAB + TAB)
                .append(StringUtils.format("{} packet = new {}();", protocolClazzName, protocolClazzName))
                .append(LS);

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            IFieldRegistration fieldRegistration = fieldRegistrations[i];

            String readObject = csSerializer(fieldRegistration.serializer()).readObject(csBuilder, 3, field, fieldRegistration);
            csBuilder.append(TAB + TAB + TAB)
                    .append(StringUtils.format("packet.{} = {};", field.getName(), readObject))
                    .append(LS);
        }

        csBuilder.append(TAB + TAB + TAB).append("return packet;").append(LS);

        csBuilder.append(TAB + TAB).append("}").append(LS);

        return csBuilder.toString();
    }


}
