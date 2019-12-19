package com.udacity.course3.reviews.repositories;

import com.udacity.course3.reviews.entity.MongoReview;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MongoReviewRepository extends MongoRepository<MongoReview, String> {
    List<MongoReview> findAll(Example example);
}
