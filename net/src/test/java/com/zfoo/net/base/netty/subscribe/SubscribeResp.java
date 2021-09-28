package com.zfoo.net.base.netty.subscribe;

import java.io.Serializable;

/**
 * @author islandempty
 * @since 2021 8
 */
public class SubscribeResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private int respID;

    private int respCode;

    private String desc;

    public SubscribeResp(int respID, int respCode, String desc) {
        this.respID = respID;
        this.respCode = respCode;
        this.desc = desc;
    }

    public int getRespID() {
        return respID;
    }

    public void setRespID(int respID) {
        this.respID = respID;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SubscribeResp{" +
                "respID=" + respID +
                ", respCode=" + respCode +
                ", desc='" + desc + '\'' +
                '}';
    }
}
