package com.phucx.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
public class VerificationInfo {
    private Boolean phoneVerified;
    private Boolean profileVerified;
}
