package com.atmmachine.model.request;

import com.atmmachine.model.Currency;
import lombok.Getter;


@Getter
public class DepositOrWithdrawOperation {
    private Double amount;
    private Currency currency;
}
