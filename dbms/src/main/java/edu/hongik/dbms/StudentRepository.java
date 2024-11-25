package edu.hongik.dbms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByName(String name);
    long countByDegree(String degree);
    
    @Query("SELECT COALESCE(MAX(s.sid), 0) FROM Student s")
    int findMaxSid(); // 현재 테이블에서 최대 sid 값을 조회
    
    @Query("SELECT COUNT(s) > 0 FROM Student s WHERE s.name = :name AND s.email = :email")
    boolean existsByNameAndEmail(@Param("name") String name, @Param("email") String email);
}
