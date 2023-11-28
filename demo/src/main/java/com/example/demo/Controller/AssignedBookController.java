package com.example.demo.Controller;

import com.example.demo.Dto.AssignedBookDTO;
import com.example.demo.Dto.CheckoutDto;
import com.example.demo.Service.AssignedBookService;
import com.example.demo.Util.CustomException;
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
    private AssignedBookService assignedBookService;

    private ResponseEntity<List<AssignedBookDTO>> handleExceptionResponse(CustomException e) {
        return new ResponseEntity<>(List.of(new AssignedBookDTO()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<List<AssignedBookDTO>> getAllAssignedBooks() {
        try {
            List<AssignedBookDTO> assignedBooks = assignedBookService.getAllAssignedBooks();
            return new ResponseEntity<>(assignedBooks, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignedBookDTO> getAssignedBookById(@PathVariable Integer id) {
        try {
            AssignedBookDTO assignedBookDTO = assignedBookService.getAssignedBookById(id);
            return new ResponseEntity<>(assignedBookDTO, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<AssignedBookDTO> saveAssignedBook(@RequestBody AssignedBookDTO assignedBookDTO) {
        try {
            AssignedBookDTO savedAssignedBook = assignedBookService.saveAssignedBook(assignedBookDTO);
            return new ResponseEntity<>(savedAssignedBook, HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignedBook(@PathVariable Integer id) {
        try {
            assignedBookService.deleteAssignedBook(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<AssignedBookDTO> checkoutBook(@RequestBody CheckoutDto checkoutDto) {
        try {
            AssignedBookDTO assignedBookDTO = assignedBookService.checkoutBook(checkoutDto.getBookId(), checkoutDto.getStudentId());
            return new ResponseEntity<>(assignedBookDTO, HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnBook(@RequestParam int bookId) {
        try {
            assignedBookService.returnBook(bookId);
            return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>("Error returning book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/checked-out/{bookId}")
    public ResponseEntity<AssignedBookDTO> getAssignedBookDetails(@PathVariable Integer bookId) {
        try {
            AssignedBookDTO assignedBookDetails = assignedBookService.getAssignedBookDetails(bookId);
            return new ResponseEntity<>(assignedBookDetails, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
