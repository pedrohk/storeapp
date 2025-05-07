package org.mystore.service;

import org.mystore.model.Subcategory;
import org.mystore.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    private final ProductCategoryService productCategoryService;

    @Autowired
    public SubcategoryService(SubcategoryRepository subcategoryRepository, ProductCategoryService productCategoryService) {
        this.subcategoryRepository = subcategoryRepository;
        this.productCategoryService = productCategoryService;
    }

    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }

    public Optional<Subcategory> getSubcategoryById(Long id) {
        return subcategoryRepository.findById(id);
    }

    public Subcategory createSubcategory(Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }

    public Optional<Subcategory> updateSubcategory(Long id, Subcategory updatedSubcategory) {
        return subcategoryRepository.findById(id)
                .map(subcategory -> {
                    subcategory.setName(updatedSubcategory.getName());
                    subcategory.setDescription(updatedSubcategory.getDescription());
                    if (updatedSubcategory.getCategory() != null && updatedSubcategory.getCategory().getId() != null) {
                        productCategoryService.getProductCategoryById(updatedSubcategory.getCategory().getId())
                                .ifPresent(subcategory::setCategory);
                    }
                    return subcategoryRepository.save(subcategory);
                });
    }

    public boolean deleteSubcategory(Long id) {
        if (subcategoryRepository.existsById(id)) {
            subcategoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}