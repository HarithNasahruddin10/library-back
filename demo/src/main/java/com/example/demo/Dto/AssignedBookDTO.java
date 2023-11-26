package com.example.demo.Dto;

import com.example.demo.Utility.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedBookDTO {

    private Integer assignedBookId;
    private String dateBorrow;
    private String dateReturn;
    private Boolean isReturned;

    private Integer studentId;
    private String studentFirstName;
    private String studentLastName;
    private String studentStudentId;
    private String studentEmail;
    private String studentPhoneNum;

    private Integer bookId;
    private String bookTitle;
    private String bookDesc;
    private String bookAuthor;
    private Category bookCategory;
    private Integer bookYearPublish;
    private String bookImage;
}
