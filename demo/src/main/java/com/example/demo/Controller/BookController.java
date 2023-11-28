// BookController.java
package com.example.demo.Controller;

import com.example.demo.Dto.BookDto;
import com.example.demo.Model.Book;
import com.example.demo.Service.BookService;
import com.example.demo.Util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookController {

    @Autowired
    private BookService bookService;

    private static final String BOOK_NOT_FOUND = "Book not found with id: ";

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            return ResponseEntity.ok(books);
        } catch (CustomException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        try {
            Book book = bookService.getBookById(id)
                    .orElseThrow(() -> new CustomException(BOOK_NOT_FOUND + id));
            return ResponseEntity.ok(book);
        } catch (CustomException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@ModelAttribute BookDto bookDto) {
        try {
            Book book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setDesc(bookDto.getDesc());
            book.setAuthor(bookDto.getAuthor());
            book.setCategory(bookDto.getCategory());
            book.setYearpublish(Integer.parseInt(bookDto.getYearPublish()));

            MultipartFile file = bookDto.getBookImage1();

            System.out.println(book.getCategory());
            System.out.println("Saving book: " + book);
            Book savedBook = bookService.saveBook(book, file);
            return ResponseEntity.status(201).body(savedBook);
        } catch (CustomException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (CustomException e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getBookImage(@PathVariable Integer id) {
        try {
            Resource resource = (Resource) bookService.getBookImage(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + resource.getFilename())
                    .body(resource);
        } catch (CustomException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Book>> getAvailableBooks() {
        try {
            List<Book> availableBooks = bookService.getAvailableBooks();
            return ResponseEntity.ok(availableBooks);
        } catch (CustomException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/checked-out")
    public ResponseEntity<List<Book>> getCheckedOutBooks() {
        try {
            List<Book> checkedOutBooks = bookService.getCheckedOutBooks();
            return ResponseEntity.ok(checkedOutBooks);
        } catch (CustomException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
