package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.product.ProductRequest;
import com.example.yuriy_ivanov.dto.product.ProductResponse;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final Converter converter;


    public ProductResponse create(ProductRequest productRequest) {
        Product bag = objectMapper.convertValue(productRequest, Product.class);
        productRepository.save(bag);

        return objectMapper.convertValue(bag, ProductResponse.class);
    }

    public List<ProductResponse> all(Pageable pageable){
        List<ProductResponse> bags = new ArrayList<>();
        for(Product bag : productRepository.findAll(pageable)) {
            bags.add(objectMapper.convertValue(bag, ProductResponse.class));
        }

        return bags;
    }

    public ProductResponse findById(Long id) {
        Product bag = productRepository.findById(id).get();

        return objectMapper.convertValue(bag, ProductResponse.class);
    }

    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product bag = productRepository.findById(id).get();
        bag.setBrand(productRequest.getBrand());
        bag.setCount(productRequest.getCount());
        bag.setType(bag.getType());
        bag.setVolume(bag.getVolume());
        bag.setPrice(bag.getPrice());
        productRepository.save(bag);

        return objectMapper.convertValue(bag, ProductResponse.class);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}

