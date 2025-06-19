package pl.dmcs.rkotas.springbootlab2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<Subject> enrolledSubjects = new HashSet<>();

    // === Constructors ===
    public Student() {}

    // === Getters & Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<Subject> getEnrolledSubjects() { return enrolledSubjects; }
    public void setEnrolledSubjects(Set<Subject> enrolledSubjects) {
        this.enrolledSubjects = enrolledSubjects;
    }

    // === Helper Methods ===
    public void enrollInSubject(Subject subject) {
        this.enrolledSubjects.add(subject);
        subject.getEnrolledStudents().add(this);
    }

    public void unenrollFromSubject(Subject subject) {
        this.enrolledSubjects.remove(subject);
        subject.getEnrolledStudents().remove(this);
    }
}
