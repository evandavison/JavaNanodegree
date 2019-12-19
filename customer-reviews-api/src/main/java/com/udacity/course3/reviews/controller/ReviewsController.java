package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.MongoReview;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.repositories.MongoReviewRepository;
import com.udacity.course3.reviews.repositories.ProductRepository;
import com.udacity.course3.reviews.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;


import javax.swing.text.Document;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
@EnableMongoRepositories
public class ReviewsController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MongoReviewRepository mongoReviewRepository;

    //Check to see if Product Exists by Id...
        //If so, with POST request and valid JSON, create a new review for the product.
        //If not return NOT FOUND error.
    @PostMapping(value = "/reviews/products/{productId}")
    public ResponseEntity<?> createReviewForProduct(@PathVariable("productId") Integer productId,
                                                    @RequestBody Review review) throws URISyntaxException {

        Optional<Product> product = productRepository.findById(productId);
        if(product.isPresent()) {
            review.setProduct(product.get());
        }

        return product.isPresent()
                ? new ResponseEntity(reviewRepository.save(review), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    //Same as above, but for Mongo Review instead of "MySQL" Review
    @PostMapping(value = "/mongoreviews/products/{productId}")
    public ResponseEntity<?> createMongoReviewForProduct(@PathVariable("productId") Integer productId,
                                                         @RequestBody MongoReview review) throws URISyntaxException {

        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            review.setProductId(product.get().getProductId());
//            product.get().getMongoReviews().add(review);?
//            productRepository.save(product.get());
        }

        return product.isPresent()
                ? new ResponseEntity(mongoReviewRepository.save(review), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Check to see if a product exists by Id...
        //If so, return the list of reviews for the product.
        //If not return NOT FOUND error.
    @GetMapping(value = "/reviews/products/{productId}")
    public ResponseEntity<List<?>> listReviewsForProduct(@PathVariable("productId") Integer productId) {

        //find product from mySql repository
        Optional<Product> product = productRepository.findById(productId);

        //find reviews by product id in mongodb
        Example<MongoReview> byProduct = Example.of(new MongoReview(productId));
        List<MongoReview> mongoReviews = mongoReviewRepository.findAll(byProduct);

        //new list to hold both types of reviews
        List reviews = new ArrayList<>();
        if(product.isPresent() & mongoReviews.size() > 0) {
            reviews.add(product.get().getReview());
            reviews.add(mongoReviews);
        } else if (product.isPresent()) {
            //executes if there are no reviews in mongodb
            reviews = product.get().getReview();
        }

        return product.isPresent()
                ? new ResponseEntity(reviews, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}