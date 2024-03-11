package com.phucx.shop.model;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFilter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data @Entity
@JsonFilter("CategoriesFilter")
public class Categories {
    public Categories() {
    }
    
    public Categories(Integer categoryID, String categoryName, String description, byte[] picture) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
        this.picture = picture;
    }

    public Categories(Categories categories) {
        this.categoryID = categories.getCategoryID();
        this.categoryName = categories.getCategoryName();
        this.description = categories.getDescription();
        this.picture = categories.getPicture();
    }

    @Id
    @GeneratedValue(generator = "native", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "CategoryID", nullable = false)
    private Integer categoryID;
    @Column(name = "CategoryName", length = 15, nullable = false)
    private String categoryName;
    private String description;
    @Lob()
    private byte[] picture;

    

}
