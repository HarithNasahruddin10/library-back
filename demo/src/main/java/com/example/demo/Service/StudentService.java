// StudentService.java
package com.example.demo.Service;

import com.example.demo.Model.Student;
import com.example.demo.Repository.StudentRepository;
import com.example.demo.Util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    private static final String STUDENT_NOT_FOUND = "Student not found with id: ";

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Integer id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Integer id) {
        studentRepository.deleteById(id);
    }

    public Student updateStudent(Integer id, Student updatedStudent) throws CustomException {
        Optional<Student> existingStudentOptional = studentRepository.findById(id);

        if (existingStudentOptional.isPresent()) {
            Student existingStudent = existingStudentOptional.get();
            // Save the updated student
            return studentRepository.save(existingStudent);
        } else {
            throw new CustomException(STUDENT_NOT_FOUND + id);
        }
    }
}
