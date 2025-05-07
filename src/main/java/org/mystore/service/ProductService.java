package org.mystore.service;

import org.mystore.model.Product;
import org.mystore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SubcategoryService subcategoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, SubcategoryService subcategoryService) {
        this.productRepository = productRepository;
        this.subcategoryService = subcategoryService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setUnitPrice(updatedProduct.getUnitPrice());
                    product.setStockQuantity(updatedProduct.getStockQuantity());
                    product.setSku(updatedProduct.getSku());
                    product.setActive(updatedProduct.isActive());
                    if (updatedProduct.getSubcategory() != null && updatedProduct.getSubcategory().getId() != null) {
                        subcategoryService.getSubcategoryById(updatedProduct.getSubcategory().getId())
                                .ifPresent(product::setSubcategory);
                    }
                    product.setUpdatedAt(java.time.LocalDateTime.now());
                    return productRepository.save(product);
                });
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}