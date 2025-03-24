package com.newtonbox.Repository;

import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.Participant;
import com.newtonbox.Models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByUser(UserEntity user);
    List<Participant> findByExperiment(Experiment experiment);
    Optional<Participant> findByExperimentAndUser(Experiment experiment, UserEntity user);
}

