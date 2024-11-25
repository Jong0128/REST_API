package edu.hongik.dbms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // (1) 특정 이름을 가진 학생의 학위 유형 질의
    @GetMapping("/degree")
    public String getStudentDegree(@RequestParam String name) {
        List<Student> students = studentRepository.findByName(name);
        if (students.isEmpty()) {
            return "No such student";
        } else if (students.size() > 1) {
            return "There are multiple students with the same name. Please provide an email address instead.";
        }
        return students.get(0).getName() + " : " + students.get(0).getDegree();
    }

    // (2) 특정 이름을 가진 학생의 이메일 질의
    @GetMapping("/email")
    public String getStudentEmail(@RequestParam String name) {
        List<Student> students = studentRepository.findByName(name);
        if (students.isEmpty()) {
            return "No such student";
        } else if (students.size() > 1) {
            return "There are multiple students with the same name. Please contact the administrator by phone.";
        }
        return students.get(0).getName() + " : " + students.get(0).getEmail();
    }

    // (3) 학위별 학생의 수 반환
    @GetMapping("/stat")
    public String getStudentCountByDegree(@RequestParam String degree) {
        long count = studentRepository.countByDegree(degree);
        return "Number of " + degree + " students : " + count;
    }

 // (4) 신규 학생 등록
    @PutMapping("/register")
    public String registerStudent(@RequestParam String name, @RequestParam String email, @RequestParam int graduation) {
        // 이미 동일한 이름과 이메일을 가진 학생이 있는지 확인
        List<Student> students = studentRepository.findByName(name);
        for (Student student : students) {
            if (student.getEmail().equals(email)) {
                return "Already registered";
            }
        }

        // 현재 최대 sid 값 가져오기
        int maxSid = studentRepository.findMaxSid(); // 데이터베이스에서 최대 sid 값 가져오기
        int newSid = maxSid + 1; // 새로운 sid 계산

        // 새로운 학생 등록
        Student newStudent = new Student();
        newStudent.setSid(newSid); // sid 설정
        newStudent.setName(name);
        newStudent.setEmail(email);
        newStudent.setDegree("Unknown"); // 학위는 기본값 설정
        newStudent.setGraduation(graduation);

        // 데이터베이스에 저장
        studentRepository.save(newStudent);

        return "Registration successful";
    }

}
