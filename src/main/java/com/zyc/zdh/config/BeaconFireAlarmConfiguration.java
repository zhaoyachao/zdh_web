package com.zyc.zdh.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeaconFireAlarmConfiguration {

    @Value("${beacon.fire.sms.regin.id}")
    private String reginId;
    @Value("${beacon.fire.sms.ak}")
    private String ak;
    @Value("${beacon.fire.sms.sk}")
    private String sk;
    @Value("${beacon.fire.sms.template}")
    private String template;
    @Value("${beacon.fire.sms.sign}")
    private String sign;


    public String getReginId() {
        return reginId;
    }

    public void setReginId(String reginId) {
        this.reginId = reginId;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}