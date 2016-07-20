package com.faayda.models;

/**
 * Created by Antonio on 15-08-2015.
 */
public class CategoryGridModel {


    int categoryImage;
    String categoryTitle, categoryImageString, categoryId,categoryImageUrl;
    Boolean isSelected = false;

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(String categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }


    public void setCategoryImageString(String categoryImageString) {
        this.categoryImageString = categoryImageString;
    }

    public String getCategoryImageString() {
        return categoryImageString;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    public void setCategoryImage(String categoryImageString) {
        this.categoryImageString = categoryImageString;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setcategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
