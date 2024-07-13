package com.phucx.keycloakmanagement.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import com.phucx.keycloakmanagement.constant.WebConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoleConverter implements Converter<Jwt, Collection<GrantedAuthority>>{

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt token) {
        log.info("convert(userID={})", token.getSubject());
        Map<String, Object> realmAccess = token.getClaimAsMap(WebConstant.REALM_ACCESS);
        if(realmAccess==null) return null;
        List<String> roles = (List<String>) realmAccess.get(WebConstant.ROLES);
        if(roles==null) return null;
        List<GrantedAuthority> authorities = roles.stream()
            .map(role ->  WebConstant.ROLE_PREFIX+role.toUpperCase())
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        return authorities;
    }

    
}
