package com.phucx.account.service.employees;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.phucx.account.model.Employees;
import com.phucx.account.repository.EmployeesRepository;
import com.phucx.account.service.github.GithubService;

@Service
public class EmployeesServiceImp implements EmployeesService {
    @Autowired
    private GithubService githubService;
    @Autowired
    private EmployeesRepository employeesRepository;

	@Override
	public Employees getEmployeeDetail(String employeeID) {
		var employeeOP = employeesRepository.findById(employeeID);
        if(employeeOP.isPresent()) return employeeOP.get();
        return null;
    }

	@Override
	public boolean createEmployee(String employeeID) {
		var employeeOp = employeesRepository.findById(employeeID);
        if(employeeOp.isEmpty()){
            Employees employee = new Employees();
            employee.setEmployeeID(employeeID);
        }
        return false;
	}

	@Override
	public boolean updateEmployeeInfo(Employees employee) {
		try {
            var fetchedEmployeeOp = employeesRepository
                .findById(employee.getEmployeeID());
            if(fetchedEmployeeOp.isPresent()){
                Employees fetchedEmployee = fetchedEmployeeOp.get();
                String picture = employee.getPhoto();
                if(picture!=null){
                    if(fetchedEmployee.getPhoto()==null){
                        picture = githubService.uploadImage(picture);
                    }else{
                        int comparedPicture =fetchedEmployee.getPhoto()
                            .compareToIgnoreCase(picture);
                        if(comparedPicture!=0){
                            picture = githubService.uploadImage(picture);
                        }else if(comparedPicture==0){
                            picture = fetchedEmployee.getPhoto();
                        }
                    }
                }
                Integer check = employeesRepository.updateEmployee(
                    employee.getFirstName(), employee.getLastName(),
                    employee.getBirthDate(), employee.getAddress(), 
                    employee.getCity(), employee.getRegion(), employee.getCountry(),
                    employee.getHomePhone(), picture, employee.getEmployeeID());
                if(check>0){
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
	}

	@Override
	public Page<Employees> findAllEmployees(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
        var employees = employeesRepository.findAll(pageable);
        return employees;
	}
    
}
