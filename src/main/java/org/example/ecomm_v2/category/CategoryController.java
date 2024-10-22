package org.example.ecomm_v2.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Get all top-level categories (no parent category)
    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();  // Returns List<CategoryDTO>
    }

    // Get subcategories of a category
    @GetMapping("/{categoryId}/subcategories")
    public List<CategoryDTO> getSubcategories(@PathVariable Long categoryId) {
        CategoryDTO parentCategory = categoryService.getCategoryById(categoryId);
        return parentCategory.getSubcategoryIds().stream()
                .map(id -> categoryService.getCategoryById(id))  // Fetch subcategory by ID
                .collect(Collectors.toList());
    }

    // Get a single category by ID
    @GetMapping("/{categoryId}")
    public CategoryDTO getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);  // Returns CategoryDTO
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);  // Returns CategoryDTO
    }

    // Update an existing category
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return ResponseEntity.ok(updatedCategory);  // Returns updated CategoryDTO
    }

    // Delete a category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
