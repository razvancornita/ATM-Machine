package com.atmmachine.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChangePinRequest {
    private String oldPin;
    private String newPin;
}
