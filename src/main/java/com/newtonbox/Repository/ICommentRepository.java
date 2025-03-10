package com.newtonbox.Repository;

import com.newtonbox.Models.Comment;
import com.newtonbox.Models.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByExperiment(Experiment experiment);

}
