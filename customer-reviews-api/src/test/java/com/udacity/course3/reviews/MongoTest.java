package com.udacity.course3.reviews;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.udacity.course3.reviews.entity.MongoReview;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;
    @DisplayName("given object to save"
            + " when save object using MongoDB template"
            + " then object is saved")
    @Test
    public void mongoRepositoryTest() {
        // given

//        MongoReview mongoReview = getMongoReview();
//
//        mongoTemplate.save(mongoReview, "collection");
//        MongoReview found = mongoTemplate.findById(mongoReview.get_id(), MongoReview.class);
//
//        // then
//        assertNotNull(found);
//        assertEquals(mongoReview.getMongoReview(), found.getMongoReview());
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("key", "value")
                .get();

        // when
        mongoTemplate.save(objectToSave, "collection");

        // then
        assertThat(mongoTemplate.findAll(DBObject.class, "collection")).extracting("key")
                .containsOnly("value");
    }


    private MongoReview getMongoReview(){
        MongoReview mongoReview = new MongoReview();
        mongoReview.setProductId(1);
        mongoReview.setMongoReview("This is a test of the testing kind.");
        return mongoReview;
    };
}
