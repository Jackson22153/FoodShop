package com.phucx.shop.config;

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

public class RoleConverter implements Converter<Jwt, Collection<GrantedAuthority>>{

    @Override
    @Nullable
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt token) {
        Map<String, Object> roles = (Map<String, Object>) token.getClaims().get("realm_access");
        if(roles!=null){
            List<GrantedAuthority> userRoles = ((List<String>) roles.get("roles")).stream()
                .map(x-> new SimpleGrantedAuthority("ROLE_"+x.toUpperCase())).collect(Collectors.toList());
            return userRoles;
        }

        return new ArrayList<>();
    }
    
}
