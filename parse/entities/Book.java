package com.diandian.childrenread.entity;

import com.diandian.childrenread.base.BaseEntity;

public class Book extends BaseEntity {

    /**
     * 绘本类型
     * [where]
     */
    private long categoryId;
    private String surfaceImg;
    private boolean isFree;
    private String name;
    private String engName;
    private boolean isHot;
    private boolean isNew;
    private boolean isOnly;
    /**
     * 年龄范围
     * [where]
     * 0 - 全部年龄
     * 1 - 0~3岁
     * 2 - 4~6岁
     * 3 - 7岁+
     */
    private int ageRange;

    private boolean isBookSet;
    private Book parentBookSet;
    private int unitSize;

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
