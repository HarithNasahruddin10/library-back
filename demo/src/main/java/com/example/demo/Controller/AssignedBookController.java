package com.example.demo.Controller;
import com.example.demo.Dto.AssignedBookDTO;
import com.example.demo.Service.AssignedBookService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
