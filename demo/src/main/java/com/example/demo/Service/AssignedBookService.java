package com.example.demo.Service;
import com.example.demo.Dto.AssignedBookDTO;
import com.example.demo.Model.AssignedBook;
import com.example.demo.Model.Book;
import com.example.demo.Model.Student;
import com.example.demo.Repository.AssignedBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignedBookService {
    @Autowired
    AssignedBookRepository assignedBookRepository;


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
