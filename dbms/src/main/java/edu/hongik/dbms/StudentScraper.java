package edu.hongik.dbms;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StudentScraper {

    @Autowired
    private StudentRepository studentRepository;

    private int sid; // 전역 변수로 sid 관리

    @PostConstruct
    public void scrapeAndSaveStudents() {
        String url = "https://apl.hongik.ac.kr/lecture/dbms";
        List<Student> students = new ArrayList<>();

        try {
            // 현재 최대 sid 값 가져오기
            sid = studentRepository.findMaxSid() + 1;

            // HTML 문서 가져오기
            Document doc = Jsoup.connect(url).get();

            // <ul> 태그 중 class="n8H08c UVNKR"인 요소만 선택
            Elements ulElements = doc.select("ul.n8H08c.UVNKR");

            // 학위 매핑 (ul 순서대로 PhD, Master, Undergraduate)
            String[] degrees = {"PhD", "Master", "Undergraduate"};

            for (int i = 0; i < ulElements.size() && i < degrees.length; i++) {
                String degree = degrees[i]; // 학위 매핑
                Element ulElement = ulElements.get(i);

                // 각 ul 내부의 li 태그 가져오기
                Elements liElements = ulElement.select("li");
                for (Element liElement : liElements) {
                    String text = liElement.text(); // li 태그 내부 텍스트
                    Student student = parseStudentData(text, degree);
                    if (student != null) {
                        // 중복 확인
                        if (!studentRepository.existsByNameAndEmail(student.getName(), student.getEmail())) {
                            student.setSid(sid++); // sid 증가
                            students.add(student);
                        } else {
                            System.out.println("Duplicate found: " + student.getName() + " (" + student.getEmail() + ")");
                        }
                    }
                }
            }

            // 데이터베이스에 저장
            studentRepository.saveAll(students);
            System.out.println("Students data inserted successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 학생 정보를 파싱하는 메서드
    private Student parseStudentData(String text, String degree) {
        String[] parts = text.split(",\\s*"); // 쉼표로 분리
        String name = parts[0]; // 첫 번째 부분: 이름
        String email = null;
        int graduation = -1;

        // 두 번째 부분이 이메일인지 확인
        if (parts.length > 1) {
            String secondPart = parts[1];
            if (secondPart.contains("@")) {
                email = secondPart; // 이메일로 저장
            }
        }

        // 마지막 부분이 졸업 연도인지 확인
        if (parts.length > 2) {
            try {
                graduation = Integer.parseInt(parts[parts.length - 1]); // 마지막 부분을 졸업 연도로 저장
            } catch (NumberFormatException e) {
                System.out.println("Invalid graduation year: " + parts[parts.length - 1]);
            }
        }

        // Student 객체 생성
        return new Student(0, name, email, degree, graduation); // sid는 이후 설정
    }
}
