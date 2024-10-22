package org.example.ecomm_v2.category;

import org.example.ecomm_v2.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.ecomm_v2.product.Product;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all top-level categories
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findByParentCategoryIsNull()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get a category by ID
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return convertToDTO(category);
    }

    // Create a new category
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());

        // Fetch parent category if it exists
        if (categoryDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + categoryDTO.getParentCategoryId()));
            category.setParentCategory(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToDTO(savedCategory);
    }

    // Update an existing category
    public CategoryDTO updateCategory(Long id, CategoryDTO updatedCategoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(updatedCategoryDTO.getName());

        // Fetch parent category if it exists
        if (updatedCategoryDTO.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(updatedCategoryDTO.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + updatedCategoryDTO.getParentCategoryId()));
            category.setParentCategory(parentCategory);
        } else {
            category.setParentCategory(null);
        }

        return convertToDTO(categoryRepository.save(category));
    }

    // Delete a category
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(category);
    }

    // Helper method to convert Category to CategoryDTO
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        if (category.getParentCategory() != null) {
            dto.setParentCategoryId(category.getParentCategory().getId());
        }

        // Populate subcategory IDs
        if (category.getSubcategories() != null) {
            List<Long> subcategoryIds = category.getSubcategories().stream()
                    .map(Category::getId)  // Get IDs of subcategories
                    .collect(Collectors.toList());
            dto.setSubcategoryIds(subcategoryIds);
        }

        // Populate product IDs
        if (category.getProducts() != null) {
            List<Long> productIds = category.getProducts().stream()
                    .map(Product::getId)  // Get IDs of products in the category
                    .collect(Collectors.toList());
            dto.setProductIds(productIds);
        }

        return dto;
    }
}


