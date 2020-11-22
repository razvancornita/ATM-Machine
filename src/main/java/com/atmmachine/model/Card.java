package com.atmmachine.model;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private int id;
    private int bankAccountId;
    private String pin;
}