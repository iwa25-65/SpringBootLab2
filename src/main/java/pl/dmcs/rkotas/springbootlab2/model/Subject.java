package pl.dmcs.rkotas.springbootlab2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Grade> grades = new HashSet<>();

    @ManyToMany(mappedBy = "enrolledSubjects")
    private Set<Student> enrolledStudents = new HashSet<>();

    // Constructors
    public Subject() {}

    public Subject(String name, String code, Teacher teacher) {
        this.name = name;
        this.code = code;
        this.teacher = teacher;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Grade> getGrades() {
        return grades;
    }

    public Set<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    // Relationship Management
    public void addGrade(Grade grade) {
        grades.add(grade);
        grade.setSubject(this);
    }

    public void removeGrade(Grade grade) {
        grades.remove(grade);
        grade.setSubject(null);
    }

    public void enrollStudent(Student student) {
        enrolledStudents.add(student);
        student.getEnrolledSubjects().add(this);
    }

    public void unenrollStudent(Student student) {
        enrolledStudents.remove(student);
        student.getEnrolledSubjects().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) &&
                Objects.equals(code, subject.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
