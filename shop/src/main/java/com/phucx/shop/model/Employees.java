package com.phucx.shop.model;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFilter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity @Data @ToString
@Table(name = "Employees")
@JsonFilter("Employees")
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

    @Column(name = "TitleOfCourtesy", length = 25)
    private String titleOfCourtesy;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BirthDate")
    private LocalDate birthDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "HireDate")
    private LocalDate hireDate;
    @Column(name = "Address", length = 60)
    private String address;
    @Column(name = "City", length = 15)
    private String city;
    @Column(name = "Region", length = 15)
    private String region;

    @Column(name = "PostalCode", length = 10)
    private String postalCode;
    @Column(name = "Country", length = 15)
    private String country;

    @Column(name = "HomePhone", length = 24)
    private String homePhone;
    @Column(name = "Extension", length = 4)
    private String extension;

    @Lob()
    private byte[] photo;

    private String notes;

    @OneToOne
    // @JsonIgnore
    @JoinColumn(name = "ReportsTo")
    private Employees reportsTo;
    
    @Column(name = "PhotoPath", nullable = true)
    private String photoPath;

    

    public Employees(String employeeID, String lastName, String firstName, String title, String titleOfCourtesy,
            LocalDate birthDate, LocalDate hireDate, String address, String city, String region, String postalCode,
            String country, String homePhone, String extension, byte[] photo, String notes, Employees reportsTo,
            String photoPath) {
        this.employeeID = employeeID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.title = title;
        this.titleOfCourtesy = titleOfCourtesy;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.homePhone = homePhone;
        this.extension = extension;
        this.photo = photo;
        this.notes = notes;
        this.reportsTo = reportsTo;
        this.photoPath = photoPath;
    }

    public Employees() {
    }
}
