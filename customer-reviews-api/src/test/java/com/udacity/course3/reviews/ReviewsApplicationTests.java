package com.udacity.course3.reviews;

import com.udacity.course3.reviews.entity.Comment;
import com.udacity.course3.reviews.entity.Product;
import com.udacity.course3.reviews.entity.Review;
import com.udacity.course3.reviews.repositories.CommentsRepository;
import com.udacity.course3.reviews.repositories.MongoReviewRepository;
import com.udacity.course3.reviews.repositories.ProductRepository;
import com.udacity.course3.reviews.repositories.ReviewRepository;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureDataJpa
public class ReviewsApplicationTests {

	@Autowired private DataSource dataSource;
	@Autowired private ProductRepository productRepository;
	@Autowired private ReviewRepository reviewRepository;
	@Autowired private CommentsRepository commentRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void controllerTests(){

		// create product, review and comment

		Product product = getProduct();
		Review review = getReview();
		Comment comment = getComment();

		//save the new product
		productRepository.save(product);
		//find the product from within the repository
		Product foundProduct = productRepository.findById(product.getProductId()).get();

		//attach the new review to the original product
		review.setProduct(foundProduct);
		reviewRepository.save(review);

		//find newly created Review by Id
		Review foundReview = reviewRepository.findById(review.getReviewId()).get();

		//create new comment and set it to the original review
		comment.setReview(foundReview);
		commentRepository.save(comment);

		//find newly created comment by comment id
		Comment foundComment = commentRepository.findById(comment.getCommentId()).get();

		//Test for existence of new product, review and comment
		assertNotNull(foundProduct);
		assertEquals(product.getProductId(), foundProduct.getProductId());

		assertNotNull(foundReview);
		assertEquals(review.getReviewId(), foundReview.getReviewId());
		assertEquals(comment.getCommentId(), foundComment.getCommentId());
	}

	private Product getProduct(){
		Product product = new Product();
		product.setProductName("CheezeIts");
		product.setProductDescription("Cheesy deliciousness");
		return product;
	}

	private Review getReview(){
		Review review = new Review();
		review.setReview("Simply put, there is no other snack that i'd rather eat");
		return review;
	}

	private Comment getComment(){
		Comment comment = new Comment();
		comment.setComment("I beg to differ, Ritz crackers are a much better snack.");
		return comment;
	}
}