package org.example.onlinevotingsystem.repositories;

import org.example.onlinevotingsystem.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
