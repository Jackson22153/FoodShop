package com.phucx.account.filters;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.phucx.account.config.WebConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
public class UserRegistrationFilter extends GenericFilter {
    private Logger logger = LoggerFactory.getLogger(UserRegistrationFilter.class);
    // @Autowired
    // private CustomersService customersService;
    // @Autowired
    // private EmployeesService employeesService;
    

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null){
            var token = (Jwt)authentication.getPrincipal();
            if(token!=null){
                String username = token.getClaimAsString(WebConfig.PREFERRED_USERNAME);
                List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
                if(roles.contains(WebConfig.ROLE_CUSTOMER)){
                    logger.info("user {} has role {}", username, WebConfig.ROLE_CUSTOMER);
                    
                }else if(roles.contains(WebConfig.ROLE_EMPLOYEE)){
                    logger.info("user {} has role {}", username, WebConfig.ROLE_EMPLOYEE);
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
}
