package com.atmmachine.model.request;

import lombok.Getter;

@Getter
public class AuthenticateRequest {
    private Integer cardId;
    private String pin;
}
