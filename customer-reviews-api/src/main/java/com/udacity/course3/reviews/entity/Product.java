package com.udacity.course3.reviews.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> review;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

//       public void setMongoReviews(List<MongoReview> mongoReviews) {
//        this.mongoReviews = mongoReviews;
//    }
//
//    public List<MongoReview> getMongoReviews() {
//        return mongoReviews;
//    }
//
//    @Column(name = "mongo_reviews")
//    private List<MongoReview> mongoReviews;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductDescription() {
        return productDescription;}
}
