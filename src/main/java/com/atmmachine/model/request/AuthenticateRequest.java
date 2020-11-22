package com.atmmachine.model.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class AuthenticateRequest {
    private Integer cardId;
    private String pin;
}
