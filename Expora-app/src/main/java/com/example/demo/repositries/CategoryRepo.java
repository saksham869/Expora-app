package com.example.demo.repositries;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

    List<Category> findByCategoryTitleContainingIgnoreCase(String keyword);

    Optional<Category> findByCategoryTitle(String categoryTitle);
 // Find categories that contain the keyword in either the title or description
    List<Category> findByCategoryTitleContainingIgnoreCaseOrCategoryDescriptionContainingIgnoreCase(String titleKeyword, String descriptionKeyword);

}
