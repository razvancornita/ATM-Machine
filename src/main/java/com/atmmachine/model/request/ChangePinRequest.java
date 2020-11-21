package com.atmmachine.model.request;

import lombok.Getter;

@Getter
public class ChangePinRequest {
    private int cardId;
    private int oldPin;
    private int newPin;
}
