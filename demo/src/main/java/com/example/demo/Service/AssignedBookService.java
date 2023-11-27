package com.example.demo.Service;
import com.example.demo.Dto.AssignedBookDTO;
import com.example.demo.Model.AssignedBook;
import com.example.demo.Model.Book;
import com.example.demo.Model.Student;
import com.example.demo.Repository.AssignedBookRepository;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Utility.BookStatus;
import com.example.demo.Utility.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignedBookService {
    @Autowired
    AssignedBookRepository assignedBookRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    StudentRepository studentRepository;


    public List<AssignedBookDTO> getAllAssignedBooks() {
        List<AssignedBook> assignedBooks = assignedBookRepository.findAll();
        return mapToDTOList(assignedBooks);
    }

    public AssignedBookDTO getAssignedBookById(Integer id) {
        Optional<AssignedBook> assignedBookOptional = assignedBookRepository.findById(id);
        return assignedBookOptional.map(this::mapToDTO).orElse(null);
    }

    public AssignedBookDTO saveAssignedBook(AssignedBookDTO assignedBookDTO) {
        AssignedBook assignedBook = mapToEntity(assignedBookDTO);
        assignedBook.getBook().setStatus(BookStatus.CHECKED_OUT);
        AssignedBook savedAssignedBook = assignedBookRepository.save(assignedBook);
        return mapToDTO(savedAssignedBook);
    }

    public void deleteAssignedBook(Integer id) {
        assignedBookRepository.deleteById(id);
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

    public AssignedBookDTO checkoutBook(Integer bookId, String studentId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        if (book.getStatus() == BookStatus.AVAILABLE) {
            AssignedBook assignedBook = new AssignedBook();
            assignedBook.setBook(book);
            assignedBook.setStudent((Student) studentRepository.findByStudentid(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId)));
            assignedBook.setDateborrow(DateUtil.getCurrentDate());  // You can replace this with actual date logic
            assignedBook.setIsreturned(false);
            assignedBook.setDatereturn("-");

            book.setStatus(BookStatus.CHECKED_OUT);
            bookRepository.save(book);
            assignedBookRepository.save(assignedBook);

            return mapToDTO(assignedBook);
        } else {
            throw new RuntimeException("Book is not available for checkout");
        }
    }

    public void returnBook(int bookId) {
        // Find the latest assigned book for the specified bookId
        AssignedBook latestAssignedBook = assignedBookRepository.findFirstByBookIdAndIsreturnedOrderByDateborrowDesc(bookId, false)
                .orElseThrow(() -> new RuntimeException("No assigned book found for the specified book"));

        // Update book status to AVAILABLE
        Book book = latestAssignedBook.getBook();
        book.setStatus(BookStatus.AVAILABLE); // Assuming you have an enum for book status
        bookRepository.save(book);

        // Mark the assigned book as returned
        latestAssignedBook.setIsreturned(true);
        assignedBookRepository.save(latestAssignedBook);
    }

    public AssignedBookDTO getAssignedBookDetails(Integer bookId) {
        Optional<AssignedBook> assignedBookOptional = assignedBookRepository.findFirstByBookIdAndIsreturnedOrderByDateborrowDesc(bookId, false);

        if (assignedBookOptional.isPresent()) {
            AssignedBook assignedBook = assignedBookOptional.get();
            Book book = bookRepository.findById(assignedBook.getBook().getId())
                    .orElseThrow(() -> new RuntimeException("Book not found with id: " + assignedBook.getBook().getId()));

            Student student = studentRepository.findById(assignedBook.getStudent().getId())
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + assignedBook.getStudent().getId()));

            return mapToDTOWithDetails(assignedBook, book, student);
        } else {
            throw new RuntimeException("No assigned book found for the specified book");
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

    }
