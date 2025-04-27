package malla.dipesh.journalApp.service;

import malla.dipesh.journalApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Configuration
public class UserRepositoryImpl {

    private final MongoTemplate mongoTemplate;

    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<User>  getUserForSA(){

        Query query = new Query();
        // by default and operator is used
        query.addCriteria(Criteria.where("email").exists(true).ne(null).ne(""));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        // for or

//        Criteria criteria = new Criteria();
//        query.addCriteria(criteria.orOperator(
//                Criteria.where("email").ne(null).ne(""),
//                Criteria.where("sentimentAnalysis").is(true))
//        );

        return mongoTemplate.find(query, User.class);
    }
}
