package com.restaurant.user.controller.dto;

import org.antlr.v4.runtime.misc.NotNull;

public record UserMoney(
        @NotNull
        Double money
) {
}
