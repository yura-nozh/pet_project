package com.example.yuriy_ivanov.services;

import com.example.yuriy_ivanov.dto.product.ProductRequest;
import com.example.yuriy_ivanov.dto.product.ProductResponse;
import com.example.yuriy_ivanov.entities.Brand;
import com.example.yuriy_ivanov.entities.Product;
import com.example.yuriy_ivanov.exception.ServiceException;
import com.example.yuriy_ivanov.exception.TypicalError;
import com.example.yuriy_ivanov.repositories.BrandRepository;
import com.example.yuriy_ivanov.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final ModelMapper mapper;
    private final BrandRepository brandRepository;
    private final BrandService brandService;

    public ProductResponse create(ProductRequest productRequest) {
        Product bag = objectMapper.convertValue(productRequest, Product.class);
        productRepository.save(bag);
        List<Product> list = new ArrayList<>();
        list.add(bag);
        bag.getBrand().setProducts(list);
        brandRepository.save(bag.getBrand());
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
        Optional<Product> bag = productRepository.findById(id);
        Product bagEntity = bag.orElseThrow(() -> new ServiceException("Product does not exist", TypicalError.PRODUCT_NOT_FOUND));

        return objectMapper.convertValue(bagEntity, ProductResponse.class);
    }

    public ProductResponse update(Long id, ProductRequest productRequest) {
        if(productRepository.findById(id).isEmpty()) {
            throw new ServiceException("This product not found", TypicalError.PRODUCT_NOT_FOUND);
        }

        Product bag = mapper.map(productRequest, Product.class);
        bag.setId(id);
        productRepository.save(bag);

        return objectMapper.convertValue(bag, ProductResponse.class);
    }

    public void delete(Long id) throws ServiceException{
        if(productRepository.findById(id).isEmpty()) {
            throw new ServiceException("Product not found", TypicalError.PRODUCT_NOT_FOUND);
        }
        else {
            Brand brand = productRepository.getById(id).getBrand();
            List<Product> list = brand.getProducts();
            list.remove(productRepository.getById(id));
            brand.setProducts(list);
            brandRepository.save(brand);
            productRepository.deleteById(id);
        }
    }
}

