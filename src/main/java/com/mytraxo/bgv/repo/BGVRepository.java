package com.mytraxo.bgv.repo;

import com.mytraxo.bgv.entity.BGVSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BGVRepository extends MongoRepository<BGVSubmission, String> {
    Optional<BGVSubmission> findByToken(String token);
        // ADD THIS LINE: Find by Application ID (Used by HR to get the link)
    Optional<BGVSubmission> findByApplicationId(String applicationId);

     BGVSubmission findByEmailAddress(String emailAddress);
     List<BGVSubmission> findByStatus(String status);
}