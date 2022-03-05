package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.BagsDto.BagRequest;
import com.example.yuriy_ivanov.dto.BagsDto.BagResponse;
import com.example.yuriy_ivanov.entities.Bag;
import com.example.yuriy_ivanov.repositories.BagsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BagService {

    private final BagsRepository bagsRepository;
    private final ObjectMapper objectMapper;

    public List<BagResponse> all(Pageable pageable){
        List<BagResponse> bags = new ArrayList<>();
        for(Bag bag : bagsRepository.findAll(pageable)) {
            bags.add(objectMapper.convertValue(bag, BagResponse.class));
        }

        return bags;
    }

    public BagResponse create(BagRequest bagRequest) {
        Bag bag = objectMapper.convertValue(bagRequest, Bag.class);
        bagsRepository.save(bag);

        return objectMapper.convertValue(bag, BagResponse.class);
    }

    public BagResponse findById(Long id) {
        Bag bag = bagsRepository.findById(id).get();

        return objectMapper.convertValue(bag, BagResponse.class);
    }

    public BagResponse update(Long id, BagRequest bagRequest) {
        Bag bag = bagsRepository.findById(id).get();
        bag.setBrand(bagRequest.getBrand());
        bag.setQuantity(bagRequest.getQuantity());
        bag.setType(bag.getType());
        bag.setVolume(bag.getVolume());
        bagsRepository.save(bag);

        return objectMapper.convertValue(bag, BagResponse.class);
    }

    public void delete(Long id) {
        bagsRepository.deleteById(id);
    }
}

