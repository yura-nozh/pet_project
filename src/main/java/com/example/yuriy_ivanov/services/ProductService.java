package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.product.ProductRequest;
import com.example.yuriy_ivanov.dto.product.ProductResponse;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.exception.ServiceException;
import com.example.yuriy_ivanov.exception.TypicalError;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper mapper;


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

    public ProductResponse findById(Long id) throws ServiceException {
        if(productRepository.findById(id).isEmpty()) {
            // TODO: 17.03.2022 orelsethrow of optional
            throw new ServiceException("Product not found", TypicalError.PRODUCT_NOT_FOUND);
        }
        Product bag = productRepository.getById(id);

        return objectMapper.convertValue(bag, ProductResponse.class);
    }

    public ProductResponse update(Long id, ProductRequest productRequest) {
        if(productRepository.findById(id).isEmpty()) {
            throw new ServiceException("Product not found", TypicalError.PRODUCT_NOT_FOUND);
        }
        // FIXED
        Product bag = mapper.map(productRequest, Product.class);
        bag.setId(id);
        productRepository.save(bag);

        return objectMapper.convertValue(bag, ProductResponse.class);
    }

    public void delete(Long id) throws ServiceException{
        if(productRepository.findById(id).isEmpty()) {
            throw new ServiceException("Product not found", TypicalError.PRODUCT_NOT_FOUND);
        }

        productRepository.deleteById(id);
    }
}

