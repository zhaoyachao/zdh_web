package com.zyc.zdh.entity;

import java.io.Serializable;

public class LoginBaseInfo implements Serializable {

    private String platform_name;

    private String background_image;

    public String getPlatform_name() {
        return platform_name;
    }

    public void setPlatform_name(String platform_name) {
        this.platform_name = platform_name;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }
}
