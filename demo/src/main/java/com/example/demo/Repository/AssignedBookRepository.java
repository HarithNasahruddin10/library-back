package com.example.demo.Repository;

import com.example.demo.Model.AssignedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AssignedBookRepository extends JpaRepository<AssignedBook, Integer> {
    // In AssignedBookRepository
    Optional<AssignedBook> findTopByBookIdOrderByDateborrowDesc(Integer bookId);

    Optional<AssignedBook> findFirstByBookIdAndIsreturnedOrderByDateborrowDesc(int bookId, boolean isReturned);

    }

