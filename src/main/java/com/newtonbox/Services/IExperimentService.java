package com.newtonbox.Services;

import com.newtonbox.Models.UserEntity;
import com.newtonbox.dto.ExperimentDTO;


import java.util.List;

public interface IExperimentService {

    List<ExperimentDTO> findAll();
    ExperimentDTO findById(Long id);
    List<ExperimentDTO> findByCreatedBy(UserEntity createdBy);   // Nombre personalizado
    ExperimentDTO save(ExperimentDTO experimentDTO);
    ExperimentDTO update(Long id, ExperimentDTO experimentDTO);
    void delete(Long id);
}
