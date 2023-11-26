package com.example.demo.Controller;

import com.example.demo.Dto.BookDto;
import com.example.demo.Model.Book;
import com.example.demo.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookController {
    @Autowired
    BookService bookService;



    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @GetMapping
    public List<Book> getAllBooks() {
        try {
            return bookService.getAllBooks();
        } catch (Exception e) {
            e.printStackTrace(); // Add this line for debugging
            throw new RuntimeException("Error fetching books", e);
        }
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Integer id) {
        return bookService.getBookById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @PostMapping
    public Book saveBook(@ModelAttribute BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setDesc(bookDto.getDesc());
        book.setAuthor(bookDto.getAuthor());
        book.setCategory(bookDto.getCategory());
        book.setYearpublish(Integer.parseInt(bookDto.getYearPublish()));

        MultipartFile file = bookDto.getBookImage1();

        System.out.println(book.getCategory());
        System.out.println("Saving book: " + book);
        return bookService.saveBook(book, file);
    }


    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getBookImage(@PathVariable Integer id) {
        // Use the new service method to get the image resource
        Resource resource = (Resource) bookService.getBookImage(id);

        // Return the image file as a response
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)  // Adjust the media type based on your image format
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + resource.getFilename())
                .body(resource);
    }
    @GetMapping("/available")
    public List<Book> getAvailableBooks() {
        // Retrieve only available books
        return bookService.getAvailableBooks();
    }

    @GetMapping("/checked-out")
    public List<Book> getCheckedOutBooks() {
        // Retrieve only checked out books
        return bookService.getCheckedOutBooks();
    }
}
