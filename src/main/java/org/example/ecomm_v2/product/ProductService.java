package org.example.ecomm_v2.product;

import org.example.ecomm_v2.ResourceNotFoundException;
import org.example.ecomm_v2.category.Category;
import org.example.ecomm_v2.category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Get all products
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)  // Convert Product entity to ProductDTO
                .collect(Collectors.toList());
    }

    // Get product by ID
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    // Create a new product
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Fetch the category from the database to ensure it exists
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDTO.getCategoryId()));

        // Create a new Product entity from DTO
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setCategory(existingCategory);  // Set the category

        // Save the product
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);  // Return the saved product as DTO
    }

    // Update an existing product
    public ProductDTO updateProduct(Long id, ProductDTO updatedProductDTO) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProductDTO.getName());
                    product.setDescription(updatedProductDTO.getDescription());
                    product.setPrice(updatedProductDTO.getPrice());
                    product.setQuantity(updatedProductDTO.getQuantity());

                    // Fetch the updated category from the database
                    Category existingCategory = categoryRepository.findById(updatedProductDTO.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + updatedProductDTO.getCategoryId()));
                    product.setCategory(existingCategory); // Update the category

                    return convertToDTO(productRepository.save(product));  // Return as DTO
                }).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    // Delete a product by ID
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public List<ProductDTO> searchByName(String keyword) {
        return productRepository.searchByName(keyword)
                .stream()
                .map(this::convertToDTO)  // Convert entities to DTOs
                .collect(Collectors.toList());
    }

    public List<ProductDTO> findByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        return productRepository.findByCategory(category)
                .stream()
                .map(this::convertToDTO)  // Convert entities to DTOs
                .collect(Collectors.toList());
    }

    public List<ProductDTO> findByPriceBetween(double minPrice, double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::convertToDTO)  // Convert entities to DTOs
                .collect(Collectors.toList());
    }

    // Helper method to convert Product to ProductDTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategoryId(product.getCategory().getId());  // Set category ID
        return dto;
    }
}
