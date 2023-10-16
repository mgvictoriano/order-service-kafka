package com.order.service.api.producer.service;

import com.order.service.api.producer.model.Product;
import com.order.service.api.producer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long idProduct, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(idProduct);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setUnitPrice(updatedProduct.getUnitPrice());
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product with ID " + idProduct + " not found.");
        }
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProduct(Long productId) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            productRepository.delete(product);
        } else {
            throw new RuntimeException("Product with ID " + productId + " not found.");
        }
    }


}
