package com.phucx.account.service.role;

import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

import com.phucx.account.exception.RoleNotFoundException;
import com.phucx.account.model.Role;

@CacheConfig(cacheNames = "role")
public interface RoleService {
    @Cacheable(key = "#roleName")
    Role getRole(String roleName) throws RoleNotFoundException;
    @Cacheable(key = "#pageNumber")
    Page<Role> getRoles(Integer pageNumber, Integer pageSize);
    @Cacheable(key = "'withoutcustomer:' + #pageNumber")
    Page<Role> getRolesWithoutCustomer(Integer pageNumber, Integer pageSize);
    @Cacheable(key = "#roleIDs")
    List<Role> getRoles(List<Integer> roleIDs);
}
