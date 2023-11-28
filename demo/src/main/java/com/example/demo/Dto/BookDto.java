package com.example.demo.Dto;

// BookDTO.java


import com.example.demo.Util.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BookDto {
    private String title;
    private String desc;
    private String author;
    private Category category;
    private String yearPublish;
    private MultipartFile bookImage1;

}
