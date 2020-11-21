package com.atmmachine.model.request;

import com.atmmachine.model.Currency;
import lombok.Getter;


@Getter
public class DepositOrWithdrawRequest {
    private Double amount;
    private Currency currency;
}
