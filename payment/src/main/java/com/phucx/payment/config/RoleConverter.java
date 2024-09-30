package com.phucx.payment.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class RoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private String REALM_ACCESS_CLAIM = "realm_access";
    private String ROLES_CLAIM = "roles";
    
    @Override
    @Nullable
    public Collection<GrantedAuthority> convert(Jwt token) {
        Map<String, Object> roles = token.getClaimAsMap(REALM_ACCESS_CLAIM);
        if(roles==null) return new ArrayList<>();

        List<GrantedAuthority> authorities =((List<String>) roles.get(ROLES_CLAIM)).stream()
            .map(role -> new SimpleGrantedAuthority("Role_" + role))
            .collect(Collectors.toList());
        return authorities;
    }

    
}
