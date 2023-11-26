package com.example.demo.Repository;

import com.example.demo.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Optional<Student> findByStudentid(String studentId);
    // You can add custom query methods if needed
}