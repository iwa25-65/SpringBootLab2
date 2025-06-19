package pl.dmcs.rkotas.springbootlab2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double value;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({"enrolledSubjects", "address", "user"})
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonIgnoreProperties({"grades", "enrolledStudents", "teacher"})
    private Subject subject;

    // === Constructors ===
    public Grade() {}

    public Grade(double value, Student student, Subject subject) {
        this.value = value;
        this.student = student;
        this.subject = subject;
    }

    public Grade(double value, String comment, Student student, Subject subject) {
        this.value = value;
        this.comment = comment;
        this.student = student;
        this.subject = subject;
    }

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    // === equals & hashCode ===
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Grade)) return false;
        Grade grade = (Grade) o;
        return Double.compare(grade.value, value) == 0 &&
                Objects.equals(id, grade.id) &&
                Objects.equals(comment, grade.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, comment);
    }

    // === toString ===
    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", value=" + value +
                ", comment='" + comment + '\'' +
                '}';
    }
}
