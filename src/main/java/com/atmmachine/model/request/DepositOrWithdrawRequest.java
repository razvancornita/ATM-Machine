package com.atmmachine.model.request;

import com.atmmachine.model.Currency;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class DepositOrWithdrawRequest {
    private Double amount;
    private Currency currency;
}
