package com.newtonbox.Services;

import com.newtonbox.Models.Result;
import com.newtonbox.Models.Experiment;
import com.newtonbox.dto.ExperimentDTO;
import com.newtonbox.dto.ResultDTO;

import java.util.List;

public interface IResultService {

    List<ResultDTO> findAll();
    ResultDTO findById(Long id);
    List<ResultDTO> findByExperiment(ExperimentDTO experimentDTO);  // Personalizado
    ResultDTO save(ResultDTO result);
    ResultDTO update(Long id, ResultDTO resultDTO);
    void delete(Long id);
}
