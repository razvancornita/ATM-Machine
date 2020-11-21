package com.atmmachine.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Card {
    private int id;
    private int bankAccountId;
    private String pin;
}