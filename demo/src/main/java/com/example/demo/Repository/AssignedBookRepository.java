package com.example.demo.Repository;

import com.example.demo.Model.AssignedBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignedBookRepository extends JpaRepository<AssignedBook, Integer> {
    // You can add custom query methods if needed
}

