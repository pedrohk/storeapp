package org.mystore.service;

import org.mystore.model.ProductCategory;
import org.mystore.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    public Optional<ProductCategory> getProductCategoryById(Long id) {
        return productCategoryRepository.findById(id);
    }

    public ProductCategory createProductCategory(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }

    public Optional<ProductCategory> updateProductCategory(Long id, ProductCategory updatedCategory) {
        return productCategoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    category.setDescription(updatedCategory.getDescription());
                    return productCategoryRepository.save(category);
                });
    }

    public boolean deleteProductCategory(Long id) {
        if (productCategoryRepository.existsById(id)) {
            productCategoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}