package com.sm.journalApp.repositories;

import com.sm.journalApp.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository // Optional, but a good practice
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findUserByUserName(String userName);
}