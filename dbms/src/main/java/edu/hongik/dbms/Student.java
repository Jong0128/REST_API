package edu.hongik.dbms;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    private int sid; // sid는 직접 관리

    private String name;
    private String email;
    private String degree;
    private int graduation;

    public Student() {}

    public Student(int sid, String name, String email, String degree, int graduation) {
        this.sid = sid;
        this.name = name;
        this.email = email;
        this.degree = degree;
        this.graduation = graduation;
    }

    // Getters and Setters
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public int getGraduation() {
        return graduation;
    }

    public void setGraduation(int graduation) {
        this.graduation = graduation;
    }
}
