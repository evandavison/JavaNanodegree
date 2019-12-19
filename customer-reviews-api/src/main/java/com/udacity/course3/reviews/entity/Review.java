package com.udacity.course3.reviews.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    @Column(name = "review")
    private String review;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Comment> comment;

    public Integer getReviewId(){return this.reviewId;}

    public void setReviewId(Integer reviewId){this.reviewId = reviewId;}

    public List<Comment> getComment(){return this.comment;}

    public void setComment(List<Comment> comment){this.comment = comment;}

    public Product getProduct(){return this.product;}

    public void setProduct(Product product){this.product = product;}

    public String getReview(){return this.review;}

    public void setReview(String review){this.review = review;}

}
