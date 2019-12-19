package com.udacity.course3.reviews.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document("mongoreviews")
public class MongoReview {

    @Id
    private String _id;

    private Integer productId;

    private String mongoReview;

    public MongoReview(Integer productId) {
        this.productId = productId;
    }

    public MongoReview(String _id){
        this._id = _id;
    }

    public MongoReview() {
    }

    private List<Comment> comments;

    public void setMongoReview(String mongoReview) {
        this.mongoReview = mongoReview;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void set_id(String reviewId) {
        this._id = _id;
    }

    public String getMongoReview() {
        return mongoReview;
    }

    public String get_id() {
        return _id;
    }
}
