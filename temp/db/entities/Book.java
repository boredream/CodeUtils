package com.diandian.childrenread.entity;

import com.diandian.childrenread.base.BaseEntity;

public class Book extends BaseEntity {

    /**
     * [id]
     */
    private String objectId;

    /**
     * 绘本类型
     * [where]
     * 1 - 整段音乐类型
     * 2 - 分段音乐+图片类型
     */
    private int musicType;

    /**
     * 绘本分类
     * [where]
     */
    private long categoryId;
    private String surfaceImg;
    private boolean isFree;
    private boolean isHot;
    private boolean isNew;
    private boolean isOnly;

    /**
     * 年龄范围
     * [where]
     * 0 - 所有年龄
     * 1 - 0~3岁
     * 2 - 4~6岁
     * 3 - 7岁+
     */
    private int ageRange;

    // type = 0 整段音乐类型，音乐地址
    private String musicUrl;
    // type = 1 分段音乐+图片绘本类型，有多个book unit
    private int unitSize;

    // 是否为绘本集类型
    private boolean isBookSet;
    // 非绘本集类型，可能有所属绘本
    private Book parentBookSet;

    /**
     * 语言类型
     * [where]
     * 0-只有中文
     * 1-中英文都有
     */
    private int languageStatus;
    private String name;
    private String engName;

    public int getMusicType() {
        return musicType;
    }

    public void setMusicType(int musicType) {
        this.musicType = musicType;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSurfaceImg() {
        return surfaceImg;
    }

    public void setSurfaceImg(String surfaceImg) {
        this.surfaceImg = surfaceImg;
    }

    public int getLanguageStatus() {
        return languageStatus;
    }

    public void setLanguageStatus(int languageStatus) {
        this.languageStatus = languageStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isOnly() {
        return isOnly;
    }

    public void setOnly(boolean only) {
        isOnly = only;
    }

    public int getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(int ageRange) {
        this.ageRange = ageRange;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public boolean isBookSet() {
        return isBookSet;
    }

    public void setBookSet(boolean bookSet) {
        isBookSet = bookSet;
    }

    public Book getParentBookSet() {
        return parentBookSet;
    }

    public void setParentBookSet(Book parentBookSet) {
        this.parentBookSet = parentBookSet;
    }

    public int getUnitSize() {
        return unitSize;
    }

    public void setUnitSize(int unitSize) {
        this.unitSize = unitSize;
    }
}
