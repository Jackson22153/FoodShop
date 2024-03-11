package com.phucx.shop.service.employees;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.shop.model.Employees;
import com.phucx.shop.repository.EmployeesRepository;

@Service
public class EmployeesServiceImp implements EmployeesService{
    @Autowired
    private EmployeesRepository employeesRepository;
    @Override
    public List<Employees> getEmployees() {
        return employeesRepository.findAll();
    }

    @Override
    public Page<Employees> getEmployees(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return employeesRepository.findAll(page);
    }

    @Override
    public Employees getEmployee(String employeeID) {
        @SuppressWarnings("null")
        var product = employeesRepository.findById(employeeID);
        if(product.isPresent()) return product.get();
        return new Employees();
    }
}
