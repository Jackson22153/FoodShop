package com.phucx.account.model;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity @Data @ToString
@Table(name = "Employees")
public class Employees{

    @Id
    @Column(name = "EmployeeID", length = 36, nullable = false)
    private String employeeID;

    @Column(name = "LastName", length = 20, nullable = false)
    private String lastName;

    @Column(name = "FirstName", length = 10, nullable = false)
    private String firstName;

    @Column(name = "Title", length = 30)
    private String title;

    // @Column(name = "TitleOfCourtesy", length = 25)
    // private String titleOfCourtesy;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BirthDate")
    private LocalDate birthDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "HireDate")
    private LocalDate hireDate;
    @Column(name = "Address", length = 200)
    private String address;
    @Column(name = "City", length = 50)
    private String city;
    // @Column(name = "Region", length = 15)
    // private String region;

    // @Column(name = "PostalCode", length = 10)
    // private String postalCode;
    // @Column(name = "Country", length = 15)
    // private String country;

    @Column(name = "HomePhone", length = 24)
    private String homePhone;
    private String photo;

    private String notes;

    @OneToOne
    // @JsonIgnore
    @JoinColumn(name = "ReportsTo")
    private Employees reportsTo;

    @OneToOne
    @JoinColumn(name = "userID")
    private Users user;

    // @ManyToMany
    // @JoinTable(
    //     name = "EmployeeTerritories",
    //     joinColumns =@JoinColumn(name="EmployeeID"),
    //     inverseJoinColumns = @JoinColumn(name="TerritoryID")
    // )
    // private List<Territories> territories;

    

    public Employees(String employeeID, String lastName, String firstName, String title,
            LocalDate birthDate, LocalDate hireDate, String address, String city, String homePhone, 
            String photo, String notes, Employees reportsTo) {
        this.employeeID = employeeID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.title = title;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.address = address;
        this.city = city;
        this.homePhone = homePhone;
        this.photo = photo;
        this.notes = notes;
        this.reportsTo = reportsTo;
    }

    public Employees() {
    }
}
