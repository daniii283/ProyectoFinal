package com.newtonbox.Repository;

import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IExperimentRepository extends JpaRepository<Experiment, Long> {
    List<Experiment> findByCreatedBy(UserEntity createdBy);
    List<Experiment> findByCreatedByUsername(String username);
}
