package com.phucx.account.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipperDTO {
    private Integer shipperID;
    private List<Integer> shipperIDs;
}   
