package com.atmmachine.model.request;

import com.atmmachine.model.Currency;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class DepositOrWithdrawRequest {
    private Double amount;
    private Currency currency;
}
