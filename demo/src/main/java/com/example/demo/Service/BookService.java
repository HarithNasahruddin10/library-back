package com.example.demo.Service;

import com.example.demo.Model.AssignedBook;
import com.example.demo.Repository.AssignedBookRepository;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Model.Book;
import com.example.demo.Util.BookStatus;
import com.example.demo.Util.Category;
import com.example.demo.Util.CustomException;
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

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AssignedBookRepository assignedBookRepository;

    @Value("${upload.directory}")
    private String uploadDirectory;

    private static final String ERROR_FETCHING_BOOKS = "Error fetching books: ";
    private static final String ERROR_FETCHING_BOOK_BY_ID = "Error fetching book by ID: ";
    private static final String ERROR_SAVING_FILE_AND_BOOK = "Failed to save the file and book details!";
    private static final String ERROR_SAVING_BOOK = "Error saving book: ";
    private static final String ERROR_DELETING_BOOK = "Error deleting book: ";
    private static final String ERROR_LOADING_IMAGE_FILE = "Error loading the image file: ";
    private static final String BOOK_NOT_FOUND = "Book not found with id: ";
    private static final String ERROR_CHECKING_BOOK_AVAILABILITY = "Error checking book availability: ";
    private static final String ERROR_FETCHING_AVAILABLE_BOOKS = "Error fetching available books: ";
    private static final String ERROR_FETCHING_CHECKED_OUT_BOOKS = "Error fetching checked-out books: ";

    public List<Book> getAllBooks() throws CustomException {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            throw new CustomException(ERROR_FETCHING_BOOKS + e.getMessage());
        }
    }

    public Optional<Book> getBookById(Integer id) throws CustomException {
        try {
            return bookRepository.findById(id);
        } catch (Exception e) {
            throw new CustomException(ERROR_FETCHING_BOOK_BY_ID + e.getMessage());
        }
    }

    public Book saveBook(Book book, MultipartFile file) throws CustomException {
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDirectory + fileName);
            Files.write(path, bytes);

            book.setBookimage(fileName);
            book.setCategory(Category.valueOf(book.getCategory().name().toUpperCase()));
            book.setStatus(BookStatus.AVAILABLE);

            return bookRepository.save(book);
        } catch (IOException e) {
            throw new CustomException(ERROR_SAVING_FILE_AND_BOOK);
        } catch (Exception e) {
            throw new CustomException(ERROR_SAVING_BOOK + e.getMessage());
        }
    }

    public void deleteBook(Integer id) throws CustomException {
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(ERROR_DELETING_BOOK + e.getMessage());
        }
    }

    public InputStreamResource getBookImage(Integer id) throws CustomException {
        Optional<Book> optionalBook = getBookById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();

            // Construct the path to the image file
            Path imagePath = Paths.get(uploadDirectory + book.getBookimage());

            // Return the input stream source directly
            try {
                return new InputStreamResource(Files.newInputStream(imagePath));
            } catch (IOException e) {
                throw new CustomException(ERROR_LOADING_IMAGE_FILE + e.getMessage());
            }
        } else {
            throw new CustomException(BOOK_NOT_FOUND + id);
        }
    }

    public boolean isBookAvailable(Integer bookId) throws CustomException {
        try {
            Optional<AssignedBook> latestAssignedBook = assignedBookRepository.findTopByBookIdOrderByDateborrowDesc(bookId);
            return latestAssignedBook.map(AssignedBook::getIsreturned).orElse(true);
        } catch (Exception e) {
            throw new CustomException(ERROR_CHECKING_BOOK_AVAILABILITY + e.getMessage());
        }
    }

    public List<Book> getAvailableBooks() throws CustomException {
        try {
            return bookRepository.findByStatus(BookStatus.AVAILABLE);
        } catch (Exception e) {
            throw new CustomException(ERROR_FETCHING_AVAILABLE_BOOKS + e.getMessage());
        }
    }

    public List<Book> getCheckedOutBooks() throws CustomException {
        try {
            return bookRepository.findByStatus(BookStatus.CHECKED_OUT);
        } catch (Exception e) {
            throw new CustomException(ERROR_FETCHING_CHECKED_OUT_BOOKS + e.getMessage());
        }
    }
}
