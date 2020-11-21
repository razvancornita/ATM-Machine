package com.atmmachine.model.request;

import lombok.Getter;

@Getter
public class ChangePinRequest {
    private String oldPin;
    private String newPin;
}
