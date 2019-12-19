package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.MongoReview;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.repositories.CommentsRepository;
import com.udacity.course3.reviews.repositories.MongoReviewRepository;
import com.udacity.course3.reviews.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsRepository commentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoReviewRepository mongoReviewRepository;

    //Check to see if a review exist by the Review Id...
        //If so return the list of comments
        //Return NOT FOUND Error
    @PostMapping(value = "/reviews/{reviewId}")
    public ResponseEntity<?> createCommentForReview(@PathVariable("reviewId") Integer reviewId,
                                                    @RequestBody Comment comment) throws URISyntaxException {

        Optional<Review> review = reviewRepository.findById(reviewId);
        if(review.isPresent()) {
            comment.setReview(review.get());
        }

        return review.isPresent()
                ? new ResponseEntity(commentRepository.save(comment), HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //creating coment for MongoReview
    @PostMapping(value = "/mongoreviews/{reviewId}")
    public ResponseEntity<?> createCommentForMongoReview(@PathVariable("reviewId") String reviewId,
                                                    @RequestBody Comment comment) throws URISyntaxException {

        Example< MongoReview > byReview = Example.of(new MongoReview(reviewId));
        Optional<MongoReview> mongoReview = mongoReviewRepository.findOne(byReview);

        if(mongoReview.isPresent()) {
            comment.setMongoReviewId(mongoReview.get().get_id());
            mongoReview.get().getComments().add(comment);
            //updates mongoReview with the new comment
                //need to save it once here to generate a comment Id
            commentRepository.save(comment);
            mongoReviewRepository.save(mongoReview.get());
        }

        return mongoReview.isPresent()
                ? new ResponseEntity(commentRepository.save(comment), HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //Find all Comments from a review Id
    @GetMapping(value = "/reviews/{reviewId}")
    public ResponseEntity<List<Comment>> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        return review.isPresent()
                ? new ResponseEntity(review.get().getComment(), HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    //Find all Comments from a mongo review Id
    @GetMapping(value = "/mongoreviews/{reviewId}")
    public ResponseEntity<List<Comment>> listCommentsForMongoReview(@PathVariable("reviewId") String reviewId) {
        Example< MongoReview > byReview = Example.of(new MongoReview(reviewId));
        Optional<MongoReview> mongoReview = mongoReviewRepository.findOne(byReview);

        return mongoReview.isPresent()
                ? new ResponseEntity(mongoReview.get().getComments(), HttpStatus.OK)
                : new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}