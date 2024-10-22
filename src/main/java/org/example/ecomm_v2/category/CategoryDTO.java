package org.example.ecomm_v2.category;

import java.util.List;

public class CategoryDTO {


    private Long id;                  // ID of the category
    private String name;              // Name of the category
    private Long parentCategoryId;    // ID of the parent category
    private List<Long> subcategoryIds; // List of subcategory IDs
    private List<Long> productIds;    // List of product IDs

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public List<Long> getSubcategoryIds() {
        return subcategoryIds;
    }

    public void setSubcategoryIds(List<Long> subcategoryIds) {
        this.subcategoryIds = subcategoryIds;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
