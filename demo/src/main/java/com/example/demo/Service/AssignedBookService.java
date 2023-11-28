package com.example.demo.Service;

import com.example.demo.Dto.AssignedBookDTO;
import com.example.demo.Model.AssignedBook;
import com.example.demo.Model.Book;
import com.example.demo.Model.Student;
import com.example.demo.Repository.AssignedBookRepository;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Util.BookStatus;
import com.example.demo.Util.DateUtil;
import com.example.demo.Util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignedBookService {

    @Autowired
    private AssignedBookRepository assignedBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StudentRepository studentRepository;

    private static final String ERROR_FETCHING_ASSIGNED_BOOKS = "Error fetching assigned books: ";
    private static final String ERROR_FETCHING_ASSIGNED_BOOK_BY_ID = "Error fetching assigned book by ID: ";
    private static final String ERROR_SAVING_ASSIGNED_BOOK = "Error saving assigned book: ";
    private static final String ERROR_DELETING_ASSIGNED_BOOK = "Error deleting assigned book: ";
    private static final String ERROR_CHECKOUT_BOOK = "Error checking out book: ";
    private static final String ERROR_RETURN_BOOK = "Error returning book: ";
    private static final String ERROR_FETCHING_ASSIGNED_BOOK_DETAILS = "Error fetching assigned book details: ";

    public List<AssignedBookDTO> getAllAssignedBooks() throws CustomException {
        try {
            List<AssignedBook> assignedBooks = assignedBookRepository.findAll();
            return mapToDTOList(assignedBooks);
        } catch (Exception e) {
            throw new CustomException(ERROR_FETCHING_ASSIGNED_BOOKS + e.getMessage());
        }
    }

    public AssignedBookDTO getAssignedBookById(Integer id) throws CustomException {
        try {
            Optional<AssignedBook> assignedBookOptional = assignedBookRepository.findById(id);
            return assignedBookOptional.map(this::mapToDTO).orElse(null);
        } catch (Exception e) {
            throw new CustomException(ERROR_FETCHING_ASSIGNED_BOOK_BY_ID + e.getMessage());
        }
    }

    public AssignedBookDTO saveAssignedBook(AssignedBookDTO assignedBookDTO) throws CustomException {
        try {
            AssignedBook assignedBook = mapToEntity(assignedBookDTO);
            assignedBook.getBook().setStatus(BookStatus.CHECKED_OUT);
            AssignedBook savedAssignedBook = assignedBookRepository.save(assignedBook);
            return mapToDTO(savedAssignedBook);
        } catch (Exception e) {
            throw new CustomException(ERROR_SAVING_ASSIGNED_BOOK + e.getMessage());
        }
    }

    public void deleteAssignedBook(Integer id) throws CustomException {
        try {
            assignedBookRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(ERROR_DELETING_ASSIGNED_BOOK + e.getMessage());
        }
    }

    public AssignedBookDTO checkoutBook(Integer bookId, String studentId) throws CustomException {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new CustomException("Book not found with id: " + bookId));

            if (book.getStatus() == BookStatus.AVAILABLE) {
                AssignedBook assignedBook = new AssignedBook();
                assignedBook.setBook(book);
                assignedBook.setStudent(studentRepository.findByStudentid(studentId)
                        .orElseThrow(() -> new CustomException("Student not found with id: " + studentId)));
                assignedBook.setDateborrow(DateUtil.getCurrentDate());
                assignedBook.setIsreturned(false);
                assignedBook.setDatereturn("-");

                book.setStatus(BookStatus.CHECKED_OUT);
                bookRepository.save(book);
                assignedBookRepository.save(assignedBook);

                return mapToDTO(assignedBook);
            } else {
                throw new CustomException("Book is not available for checkout");
            }
        } catch (Exception e) {
            throw new CustomException(ERROR_CHECKOUT_BOOK + e.getMessage());
        }
    }

    public void returnBook(int bookId) throws CustomException {
        try {
            AssignedBook latestAssignedBook = assignedBookRepository.findFirstByBookIdAndIsreturnedOrderByDateborrowDesc(bookId, false)
                    .orElseThrow(() -> new CustomException("No assigned book found for the specified book"));

            Book book = latestAssignedBook.getBook();
            book.setStatus(BookStatus.AVAILABLE);
            bookRepository.save(book);

            latestAssignedBook.setIsreturned(true);
            assignedBookRepository.save(latestAssignedBook);
        } catch (Exception e) {
            throw new CustomException(ERROR_RETURN_BOOK + e.getMessage());
        }
    }

    public AssignedBookDTO getAssignedBookDetails(Integer bookId) throws CustomException {
        try {
            Optional<AssignedBook> assignedBookOptional = assignedBookRepository.findFirstByBookIdAndIsreturnedOrderByDateborrowDesc(bookId, false);

            if (assignedBookOptional.isPresent()) {
                AssignedBook assignedBook = assignedBookOptional.get();
                Book book = bookRepository.findById(assignedBook.getBook().getId())
                        .orElseThrow(() -> new CustomException("Book not found with id: " + assignedBook.getBook().getId()));

                Student student = studentRepository.findById(assignedBook.getStudent().getId())
                        .orElseThrow(() -> new CustomException("Student not found with id: " + assignedBook.getStudent().getId()));

                return mapToDTOWithDetails(assignedBook, book, student);
            } else {
                throw new CustomException("No assigned book found for the specified book");
            }
        } catch (Exception e) {
            throw new CustomException(ERROR_FETCHING_ASSIGNED_BOOK_DETAILS + e.getMessage());
        }
    }

    private AssignedBookDTO mapToDTOWithDetails(AssignedBook assignedBook, Book book, Student student) {
        return new AssignedBookDTO(
                assignedBook.getId(),
                assignedBook.getDateborrow(),
                assignedBook.getDatereturn(),
                assignedBook.getIsreturned(),
                student.getId(),
                student.getFirstname(),
                student.getLastname(),
                student.getStudentid(),
                student.getEmail(),
                student.getPhonenum(),
                book.getId(),
                book.getTitle(),
                book.getDesc(),
                book.getAuthor(),
                book.getCategory(),
                book.getStatus(),
                book.getYearpublish(),
                book.getBookimage()
        );
    }

    private AssignedBookDTO mapToDTO(AssignedBook assignedBook) {
        return new AssignedBookDTO(
                assignedBook.getId(),
                assignedBook.getDateborrow(),
                assignedBook.getDatereturn(),
                assignedBook.getIsreturned(),
                assignedBook.getStudent().getId(),
                assignedBook.getStudent().getFirstname(),
                assignedBook.getStudent().getLastname(),
                assignedBook.getStudent().getStudentid(),
                assignedBook.getStudent().getEmail(),
                assignedBook.getStudent().getPhonenum(),
                assignedBook.getBook().getId(),
                assignedBook.getBook().getTitle(),
                assignedBook.getBook().getDesc(),
                assignedBook.getBook().getAuthor(),
                assignedBook.getBook().getCategory(),
                assignedBook.getBook().getStatus(),
                assignedBook.getBook().getYearpublish(),
                assignedBook.getBook().getBookimage()
        );
    }

    private List<AssignedBookDTO> mapToDTOList(List<AssignedBook> assignedBooks) {
        return assignedBooks.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private AssignedBook mapToEntity(AssignedBookDTO assignedBookDTO) {
        AssignedBook assignedBook = new AssignedBook();
        assignedBook.setId(assignedBookDTO.getAssignedBookId());
        assignedBook.setDateborrow(assignedBookDTO.getDateBorrow());
        assignedBook.setDatereturn(assignedBookDTO.getDateReturn());
        assignedBook.setIsreturned(assignedBookDTO.getIsReturned());

        Student student = new Student();
        student.setId(assignedBookDTO.getStudentId());
        assignedBook.setStudent(student);

        Book book = new Book();
        book.setId(assignedBookDTO.getBookId());
        assignedBook.setBook(book);

        return assignedBook;
    }
}
