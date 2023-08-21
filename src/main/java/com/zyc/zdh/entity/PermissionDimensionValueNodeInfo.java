package com.zyc.zdh.entity;

public class PermissionDimensionValueNodeInfo {

    private String id;

    private String parent;

    private String text;

    private String icon;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static PermissionDimensionValueNodeInfo build(PermissionDimensionValueInfo permissionDimensionValueInfo){
        PermissionDimensionValueNodeInfo permissionDimensionValueNodeInfo=new PermissionDimensionValueNodeInfo();
        permissionDimensionValueNodeInfo.setId(permissionDimensionValueInfo.getDim_value_code());
        permissionDimensionValueNodeInfo.setParent(permissionDimensionValueInfo.getParent_dim_value_code());
        permissionDimensionValueNodeInfo.setIcon("fa fa-folder");
        permissionDimensionValueNodeInfo.setText(permissionDimensionValueInfo.getDim_value_name());
        return permissionDimensionValueNodeInfo;
    }
}