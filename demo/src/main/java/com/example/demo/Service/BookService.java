package com.example.demo.Service;

import com.example.demo.Dto.BookDto;
import com.example.demo.Model.AssignedBook;
import com.example.demo.Repository.AssignedBookRepository;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Model.Book;
import com.example.demo.Utility.BookStatus;
import com.example.demo.Utility.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    AssignedBookRepository assignedBookRepository;
    @Value("${upload.directory}")
    private String uploadDirectory;

    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();

        return books;
    }

    public Optional<Book> getBookById(Integer id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book, MultipartFile file) {
        // Save the file to the static folder
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDirectory + fileName);
            Files.write(path, bytes);

            // Set the book image path in the book object
            book.setBookimage(fileName);
            book.setCategory(Category.valueOf(book.getCategory().name().toUpperCase()));
            book.setStatus(BookStatus.AVAILABLE);

            return bookRepository.save(book);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save the file and book details!");
        }
    }


    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }

    public InputStreamResource getBookImage(Integer id) {
        Optional<Book> optionalBook = getBookById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            // Construct the path to the image file
            Path imagePath = Paths.get(uploadDirectory + book.getBookimage());

            // Return the input stream source directly
            try {
                return new InputStreamResource(Files.newInputStream(imagePath));
            } catch (IOException e) {
                throw new RuntimeException("Error loading the image file", e);
            }
        } else {
            throw new RuntimeException("Book not found with id: " + id);
        }}


    public boolean isBookAvailable(Integer bookId) {
        Optional<AssignedBook> latestAssignedBook = assignedBookRepository.findTopByBookIdOrderByDateborrowDesc(bookId);
        return latestAssignedBook.map(AssignedBook::getIsreturned).orElse(true);
    }
    public List<Book> getAvailableBooks() {
        // Retrieve only available books
        return bookRepository.findByStatus(BookStatus.AVAILABLE);
    }

    public List<Book> getCheckedOutBooks() {
        // Retrieve only checked out books
        return bookRepository.findByStatus(BookStatus.CHECKED_OUT);
    }


}
