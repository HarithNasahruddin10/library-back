package com.example.demo.Repository;

import com.example.demo.Model.Book;
import com.example.demo.Utility.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByStatus(BookStatus bookStatus);
    // You can add custom query methods if needed
}
