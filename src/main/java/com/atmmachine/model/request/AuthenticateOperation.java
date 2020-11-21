package com.atmmachine.model.request;

import lombok.Getter;

@Getter
public class AuthenticateOperation {
    private Integer cardId;
    private String pin;
}
