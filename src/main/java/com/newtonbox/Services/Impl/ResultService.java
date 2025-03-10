package com.newtonbox.Services.Impl;

import com.newtonbox.Models.Experiment;
import com.newtonbox.Models.Result;
import com.newtonbox.Repository.IResultRepository;
import com.newtonbox.Services.IResultService;
import com.newtonbox.dto.ExperimentDTO;
import com.newtonbox.dto.ResultDTO;
import com.newtonbox.mapper.ExperimentMapper;
import com.newtonbox.mapper.ResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService implements IResultService {

    @Autowired
    private IResultRepository resultRepo;


    @Override
    public List<ResultDTO> findAll() {

        return resultRepo.findAll().stream()
                .map(ResultMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResultDTO findById(Long id) {
        Result result = resultRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with ID: " + id));
        return ResultMapper.toDTO(result);
    }

    // --- Personalizado--
    @Override
    public List<ResultDTO> findByExperiment(ExperimentDTO experimentDTO) {
        Experiment experiment = ExperimentMapper.toEntity(experimentDTO);
        return resultRepo.findByExperiment(experiment).stream()
                .map(ResultMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResultDTO save(ResultDTO resultDTO) {
        Result  result  = ResultMapper.toEntity(resultDTO);
        Result savedResult = resultRepo.save(result);
        return ResultMapper.toDTO(savedResult);
    }

    @Override
    public ResultDTO update(Long id, ResultDTO resultDTO) {
        Result existingResult = resultRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with ID: " + id));
        // Actualizamos el campo data
        if(resultDTO.getData() != null){
            existingResult.setData(resultDTO.getData());
        }

        Result updatedResult = resultRepo.save(existingResult);
        return ResultMapper.toDTO(updatedResult);
    }

    @Override
    public void delete(Long id) {
        Result result = resultRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with ID: " + id));
        resultRepo.delete(result);
    }
}
