package com.zfoo.protocol.registration;

import com.zfoo.protocol.util.AssertionUtils;
import com.zfoo.protocol.util.StringUtils;

/**
 * @author islandempty
 * @since 2021/7/9
 **/
public class ProtocolModule {

    public static final ProtocolModule DEFAULT_PROTOCOL_MODULE = new ProtocolModule((byte) 0, "default","1.0.0");

    private byte id;

    private String name;

    /**
     * 1.xxx.xxx，将1.0.0转化为1000000
     */
    private int version;

    private transient int hash;

    public ProtocolModule(byte id, String name, String version) {
        if (id < 0) {
            throw new IllegalArgumentException(StringUtils.format("模块[{}]的id[{}]必须大于0", name, id));
        }

        this.id = id;
        this.name = name;
        this.version = versionStrToNum(version);
        this.perfectHash();
    }

    public ProtocolModule(String name) {
        this.name = name;
    }

    public static void assertVersion(String version) {
        if (!version.matches("[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}")) {
            throw new IllegalArgumentException(StringUtils
                    .format("[version:{}] must like xxx.xxx.xxx", version));
        }
    }

    public static  int versionStrToNum(String version){
        assertVersion(version);
        var splits = version.split("\\" + StringUtils.PERIOD);
        var versionNum = Integer.parseInt(splits[0]) * 1_000_000 + Integer.parseInt(splits[1]) * 1_000 + Integer.parseInt(splits[2]);

        var newVersionStr = versionNumToStr(versionNum);
        AssertionUtils.isTrue(version.equals(newVersionStr), "版本号转换前[{}]和转换后不相等[{}]", version, newVersionStr);
        return versionNum;
    }
    public static String versionNumToStr(int version) {
        var versionStr = version / 1_000_000 + StringUtils.PERIOD +
                version / 1_000 % 1_000 + StringUtils.PERIOD +
                version % 1_000;
        assertVersion(versionStr);
        return versionStr;
    }
    public void perfectHash() {
        this.hash = id * 1_000_000 + this.version;
    }

    public byte getId() {
        return id;
    }

    public void setId(byte id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProtocolModule module = (ProtocolModule) o;
        return id == module.id && version == module.version;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    @Override
    public String toString() {
        return StringUtils.format("[id:{}][name:{}][version:{}][hash:{}]", id, name, version, hash);
    }
}

