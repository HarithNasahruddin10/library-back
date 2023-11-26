package com.example.demo.Controller;
import com.example.demo.Dto.AssignedBookDTO;
import com.example.demo.Dto.CheckoutDto;
import com.example.demo.Model.Book;
import com.example.demo.Service.AssignedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assigned-books")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AssignedBookController {
    @Autowired
    AssignedBookService assignedBookService;


    @GetMapping
    public List<AssignedBookDTO> getAllAssignedBooks() {
        return assignedBookService.getAllAssignedBooks();
    }

    @GetMapping("/{id}")
    public AssignedBookDTO getAssignedBookById(@PathVariable Integer id) {
        return assignedBookService.getAssignedBookById(id);
    }

    @PostMapping
    public AssignedBookDTO saveAssignedBook(@RequestBody AssignedBookDTO assignedBookDTO) {
        return assignedBookService.saveAssignedBook(assignedBookDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteAssignedBook(@PathVariable Integer id) {
        assignedBookService.deleteAssignedBook(id);
    }

    @PostMapping("/checkout")
    public AssignedBookDTO checkoutBook (@RequestBody CheckoutDto checkoutDto) {
        return assignedBookService.checkoutBook(checkoutDto.getBookId(), checkoutDto.getStudentId());
    }
//    @PostMapping("/return")
//    public ResponseEntity<String> returnBook(@RequestParam Long bookId) {
//        try {
//            assignedBookService.returnBook(bookId);
//            return ResponseEntity.ok("Book returned successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error returning book: " + e.getMessage());
//        }
//    }
@PostMapping("/return")
public ResponseEntity<String> returnBook(@RequestParam int bookId) {
    try {
        assignedBookService.returnBook(bookId);
        return ResponseEntity.ok("Book returned successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error returning book: " + e.getMessage());
    }
}


}
