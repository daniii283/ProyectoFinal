package com.newtonbox.Repository;

import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByExperiment (Experiment experiment);
}
