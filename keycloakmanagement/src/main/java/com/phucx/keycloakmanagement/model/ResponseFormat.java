package com.phucx.keycloakmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFormat {
    private Boolean status;
    private String error;
    public ResponseFormat(Boolean status) {
        this.status = status;
    }
}
