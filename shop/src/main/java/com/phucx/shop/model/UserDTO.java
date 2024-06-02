package com.phucx.shop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends DataDTO{
    private String username;
    private String userID;

    private Integer pageNumber;
    private Integer pageSize;
}
