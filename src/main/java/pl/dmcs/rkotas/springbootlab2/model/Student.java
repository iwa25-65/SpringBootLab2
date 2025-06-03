package pl.dmcs.rkotas.springbootlab2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Student {

    @Id
    @GeneratedValue
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    private Address address;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Team> teamList;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getFirstname() {return firstname;}
    public void setFirstname(String firstname) {this.firstname = firstname;}

    public String getLastname() {return lastname;}
    public void setLastname(String lastname) {this.lastname = lastname;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Team> getTeamList() {return teamList;}

    public void setTeamList(List<Team> teamList) {this.teamList = teamList;}
}
