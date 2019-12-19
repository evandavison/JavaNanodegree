package com.udacity.course3.reviews;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.MongoReview;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.repositories.CommentsRepository;
import com.udacity.course3.reviews.repositories.MongoReviewRepository;
import com.udacity.course3.reviews.repositories.ProductRepository;
import com.udacity.course3.reviews.repositories.ReviewRepository;
import org.flywaydb.core.Flyway;
import org.hibernate.dialect.Database;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

import javax.swing.text.Document;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableMongoRepositories
public class ReviewsApplication {

	public static void main(String[] args) { SpringApplication.run(ReviewsApplication.class, args);}

	@Bean
	public CommandLineRunner demo(ProductRepository productRepository,
								  CommentsRepository commentRepository,
								  ReviewRepository reviewRepository,
								  MongoReviewRepository mongoReviewRepository){

		return (args) -> {

			//setting up FlyWay
			String flywayUrl = "jdbc:mysql://localhost:3306/CUSTOMERREVIEWS?useSSL=false";
			Flyway flyway = Flyway.configure().dataSource(flywayUrl, "root", "password").load();
			flyway.repair();
			flyway.migrate();

			//Create a Product
			Product product = new Product();
			product.setProductName("Hydro");
			product.setProductDescription("One of the greatest inventions known to man kind");
			productRepository.save(product);

			//Create a review
			Review review = new Review();
			review.setReview("This thing is amazing");
			review.setProduct(product);
			reviewRepository.save(review);

			//Create some comments
			Comment comment = new Comment();
			comment.setComment("As a user of this, i definitely agree!");
			comment.setReview(review);
			commentRepository.save(comment);

			Comment comment2 = new Comment();
			comment2.setComment("Yeah why would't you agree with this review?");
			comment2.setReview(review);
			commentRepository.save(comment2);


			//Create Review for MongoDB with comments inside of it.
			MongoReview mongoReview = new MongoReview();
			mongoReview.setMongoReview("This new item is the best");
			mongoReviewRepository.save(mongoReview);

			List<Comment> comments = new ArrayList<>();

			Comment mongoComment1 = new Comment();
			mongoComment1.setComment("Wow this item really is the best!");
			mongoComment1.setMongoReviewId(mongoReview.get_id());
			commentRepository.save(mongoComment1);
			comments.add(mongoComment1);

			Comment mongoComment2 = new Comment();
			mongoComment2.setComment("Idk what this person is talking about, this thing STINKS.");
			mongoComment2.setMongoReviewId(mongoReview.get_id());
			commentRepository.save(mongoComment2);
			comments.add(mongoComment2);

			mongoReview.setComments(comments);
			mongoReview.setProductId(product.getProductId());
			mongoReviewRepository.save(mongoReview);

		};
	}

	//Prevents tests from failing...
	@Bean
	public MongoClient m() throws Exception { return new MongoClient("localhost"); }
	@Bean
	public MongoTemplate mongoTemplate() throws Exception { return new MongoTemplate(m(), "customerreviews"); }

}