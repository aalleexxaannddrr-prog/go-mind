package fr.mossaab.security.service;


import fr.mossaab.security.entities.Value;
import fr.mossaab.security.repository.AcceptableValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcceptableValueService  {

    private AcceptableValueRepository acceptableValueRepository;

    @Autowired
    public void setAcceptableValueRepository(AcceptableValueRepository acceptableValueRepository) {
        this.acceptableValueRepository = acceptableValueRepository;
    }

    public void addAll(List<Value> values) {
        if(!values.isEmpty()){
            acceptableValueRepository.saveAllAndFlush(values);
        }
    }
}