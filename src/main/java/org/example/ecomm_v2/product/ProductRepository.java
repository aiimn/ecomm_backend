package org.example.ecomm_v2.product;

import org.example.ecomm_v2.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search by product name
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);

    // Filter products by category
    List<Product> findByCategory(Category category);

    // Filter by price range
    List<Product> findByPriceBetween(double minPrice, double maxPrice);

}
