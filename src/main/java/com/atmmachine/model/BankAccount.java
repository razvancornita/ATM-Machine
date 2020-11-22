package com.atmmachine.model;

import lombok.Data;

@Data
public class BankAccount {
    private int id;
    private String ownerFirstName;
    private String ownerLastName;
    private Currency currency;
    private Double balance;
}