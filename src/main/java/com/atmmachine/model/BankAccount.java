package com.atmmachine.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BankAccount {
    private int id;
    private String ownerFirstName;
    private String ownerLastName;
    private Currency currency;
    private Double balance;
}