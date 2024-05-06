package com.phucx.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.account.constant.WebConstant;
import com.phucx.account.exception.InvalidDiscountException;
import com.phucx.account.model.Category;
import com.phucx.account.model.CustomerAccount;
import com.phucx.account.model.CustomerDetailDTO;
import com.phucx.account.model.Discount;
import com.phucx.account.model.DiscountDetail;
import com.phucx.account.model.DiscountType;
import com.phucx.account.model.DiscountWithProduct;
import com.phucx.account.model.Employee;
import com.phucx.account.model.EmployeeAccount;
import com.phucx.account.model.EmployeeDetailDTO;
import com.phucx.account.model.ProductDetails;
import com.phucx.account.model.ResponseFormat;
import com.phucx.account.model.Role;
import com.phucx.account.model.UserInfo;
import com.phucx.account.model.UserRole;
import com.phucx.account.service.category.CategoryService;
import com.phucx.account.service.customer.CustomerService;
import com.phucx.account.service.discount.DiscountService;
import com.phucx.account.service.employee.EmployeeService;
import com.phucx.account.service.product.ProductService;
import com.phucx.account.service.role.RoleService;
import com.phucx.account.service.user.UserService;


@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private ProductService productService;
    @Autowired
    private DiscountService discountsService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/product")
    public ResponseEntity<ResponseFormat> updateProductDetails(
        @RequestBody ProductDetails productDetails
    ){        
        boolean status = productService.updateProductDetails(productDetails);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }
    @PutMapping("/product")
    public ResponseEntity<ResponseFormat> insertProductDetails(
        @RequestBody ProductDetails productDetails
    ){        
        boolean status = productService.insertProductDetails(productDetails);
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }
    @GetMapping("/product/{productID}")
    public ResponseEntity<ProductDetails> getProductDetails(
        @PathVariable Integer productID
    ){        
        ProductDetails product = productService.getProductDetails(productID);
        return ResponseEntity.ok().body(product);
    }


    // discount
    @PutMapping("/discount")
    public ResponseEntity<ResponseFormat> insertDiscount(
        @RequestBody DiscountWithProduct discount
    ) throws InvalidDiscountException, RuntimeException{
        Discount newDiscount = discountsService.insertDiscount(discount);
        boolean status = newDiscount!=null?true:false;
        ResponseFormat data = new ResponseFormat(status);

        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/discount")
    public ResponseEntity<ResponseFormat> updateDiscount(
        @RequestBody DiscountWithProduct discount
    ) throws InvalidDiscountException{
        Boolean status = discountsService.updateDiscount(discount);
        ResponseFormat data = new ResponseFormat(status);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/discount/status")
    public ResponseEntity<ResponseFormat> updateDiscountStatus(
        @RequestBody Discount discount
    ) throws InvalidDiscountException{
        Boolean check = discountsService.updateDiscountStatus(discount);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/discount/product/{productID}")
    public ResponseEntity<Page<DiscountDetail>> getDiscountsByProductID(
        @PathVariable(name = "productID") Integer productID,
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber: 0;
        Page<DiscountDetail> discounts = discountsService.getDiscountsByProduct(
            productID, pageNumber,WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(discounts);
    }

    @GetMapping("/discount/{discountID}")
    public ResponseEntity<DiscountDetail> getDiscountDetail(
        @PathVariable(name = "discountID") String discountID
    ){
        DiscountDetail discount = discountsService.getDiscountDetail(discountID);
        return ResponseEntity.ok().body(discount);
    }

    @GetMapping("/discountTypes")
    public ResponseEntity<Page<DiscountType>> getDiscountTypes(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){
        pageNumber = pageNumber!=null?pageNumber: 0;
        Page<DiscountType> types = discountsService.getDiscountTypes(
            pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(types);
    }
// customers
    @GetMapping("/customers")
    public ResponseEntity<Page<CustomerAccount>> getCustomers(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "customerID", required = false) String customerID,
        @RequestParam(name = "contactName", required = false) String contactName,
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "email", required = false) String email
    ){        
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<CustomerAccount> customers = null;
        if(customerID!=null) customers = customerService.searchCustomersByCustomerID(customerID, pageNumber, WebConstant.PAGE_SIZE);
        else if(contactName!=null) customers = customerService.searchCustomersByContactName(contactName, pageNumber, WebConstant.PAGE_SIZE);
        else if(username!=null) customers = customerService.searchCustomersByUsername(username, pageNumber, WebConstant.PAGE_SIZE);
        else if(email!=null) customers = customerService.searchCustomersByEmail(email, pageNumber, WebConstant.PAGE_SIZE);
        else customers = customerService.getAllCustomers(pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(customers);
    }

    @GetMapping("/customers/{customerID}")
    public ResponseEntity<CustomerDetailDTO> getUserByCustomerID(
        @PathVariable(name = "customerID") String customerID
    ){
        CustomerDetailDTO customer = customerService.getCustomerDetailByCustomerID(customerID);
        return ResponseEntity.ok().body(customer);
    }
    // employees
    @GetMapping("/employees")
    public ResponseEntity<Page<EmployeeAccount>> getEmployees(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "employeeID", required = false) String employeeID,
        @RequestParam(name = "firstName", required = false) String firstName,
        @RequestParam(name = "lastName", required = false) String lastName,
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "email", required = false) String email
    ){        
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<EmployeeAccount> employees = null;
        if(employeeID!=null) employees = employeeService.searchEmployeesByEmployeeID(employeeID, pageNumber, WebConstant.PAGE_SIZE);
        else if(firstName!=null) employees = employeeService.searchEmployeesByFirstName(firstName, pageNumber, WebConstant.PAGE_SIZE);
        else if(lastName!=null) employees = employeeService.searchEmployeesByLastName(lastName, pageNumber, WebConstant.PAGE_SIZE);
        else if(username!=null) employees = employeeService.searchEmployeesByUsername(username, pageNumber, WebConstant.PAGE_SIZE);
        else if(email!=null) employees = employeeService.searchEmployeesByEmail(email, pageNumber, WebConstant.PAGE_SIZE);
        else employees = employeeService.getAllEmployees(pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(employees);
    }

    @GetMapping("/employees/{employeeID}")
    public ResponseEntity<EmployeeDetailDTO> getEmployeeDetail(
        @PathVariable(name = "employeeID") String employeeID
    ){
        EmployeeDetailDTO employee = employeeService.getEmployeeByID(employeeID);
        return ResponseEntity.ok().body(employee);
    }
    
    @PostMapping("/employees")
    public ResponseEntity<ResponseFormat> updateEmployeeDetail(
        @RequestBody Employee employee
    ){
        Boolean status = employeeService.updateAdminEmployeeInfo(employee);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
// users
    @GetMapping("/users")
    public ResponseEntity<Page<UserRole>> getUsers(
        @RequestParam(name = "page", required = false) Integer pageNumber,
        @RequestParam(name = "userID", required = false) String userID,
        @RequestParam(name = "roleName", required = false) String roleName,
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "email", required = false) String email
    ){  
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<UserRole> users = null;
        if(userID!=null) users = userService.searchUsersByUserID(userID, pageNumber, WebConstant.PAGE_SIZE);
        else if(username!=null) users = userService.searchUsersByUsername(username, pageNumber, WebConstant.PAGE_SIZE);
        else if(email!=null) users = userService.searchUsersByEmail(email, pageNumber, WebConstant.PAGE_SIZE);
        else if(roleName!=null) users = userService.searchUsersByRoleName(roleName, pageNumber, WebConstant.PAGE_SIZE);
        else users = userService.getAllUserRoles(pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/users/{userID}/password")
    public ResponseEntity<ResponseFormat> resetPassword(
        @PathVariable(name = "userID") String userID
    ){
        Boolean status = userService.resetPassword(userID);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @PostMapping("/users/roles")
    public ResponseEntity<ResponseFormat> assignRoles(
        @RequestBody UserInfo user
    ){
        Boolean status = userService.assignUserRoles(user);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
    // roles
    @GetMapping("/roles/employee")
    public ResponseEntity<Page<Role>> getRolesEmployee(
        @RequestParam(name = "page", required = false) Integer pageNumber
    ){  
        pageNumber = pageNumber!=null?pageNumber:0;
        Page<Role> roles = roleService.getRolesWithoutCustomer(pageNumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(roles);
    }
// category
    @PostMapping("/category")
    public ResponseEntity<ResponseFormat> updateCategory(
        @RequestBody Category category
    ){
        boolean check = categoryService.updateCategory(category);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }
    @PutMapping("/category")
    public ResponseEntity<ResponseFormat> createCategory(
        @RequestBody Category category
    ){
        boolean check = categoryService.createCategory(category);
        ResponseFormat data = new ResponseFormat(check);
        return ResponseEntity.ok().body(data);
    }
}
