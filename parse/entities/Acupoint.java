package com.boredream.cnmedicine.entity;

import com.boredream.cnmedicine.base.BaseEntity;

/**
 * 穴位
 */
public class Acupoint extends BaseEntity {

    private String bodyArea;
    private int jingLuoIndex;

    /**
     * 五输穴类型
     */
    private String wuShuType;
    private String name;
    private String pinyin;

    /**
     * 经络
     * [where]
     */
    private String jingLuo;
    private String wuShuWuXing;
    private String jingLuoIndexDirection;

    /**
     * 功能类型
     * [where]
     */
    private String functionType;
    private String location;

    public String getBodyArea() {
        return bodyArea;
    }

    public void setBodyArea(String bodyArea) {
        this.bodyArea = bodyArea;
    }

    public int getJingLuoIndex() {
        return jingLuoIndex;
    }

    public void setJingLuoIndex(int jingLuoIndex) {
        this.jingLuoIndex = jingLuoIndex;
    }

    public String getWuShuType() {
        return wuShuType;
    }

    public void setWuShuType(String wuShuType) {
        this.wuShuType = wuShuType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getJingLuo() {
        return jingLuo;
    }

    public void setJingLuo(String jingLuo) {
        this.jingLuo = jingLuo;
    }

    public String getWuShuWuXing() {
        return wuShuWuXing;
    }

    public void setWuShuWuXing(String wuShuWuXing) {
        this.wuShuWuXing = wuShuWuXing;
    }

    public String getJingLuoIndexDirection() {
        return jingLuoIndexDirection;
    }

    public void setJingLuoIndexDirection(String jingLuoIndexDirection) {
        this.jingLuoIndexDirection = jingLuoIndexDirection;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
