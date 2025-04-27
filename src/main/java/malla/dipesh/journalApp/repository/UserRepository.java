package malla.dipesh.journalApp.repository;

import lombok.NonNull;
import malla.dipesh.journalApp.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findById(@NonNull ObjectId id);

   Optional<User> findByUsername(@NonNull String username);
}
