package com.atmmachine.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.AccessDeniedException;

@Slf4j
public class AuthenticatedCard {

    @Getter
    @Setter
    private static Integer cardId;

    public static void checkIfCardIsAuthenticated() throws AccessDeniedException {
        log.debug("Checking if card is authenticated");
        if (AuthenticatedCard.cardId == null) {
            log.error("Card is not authenticated");
            throw new AccessDeniedException("Please enter a card!");
        }
        log.debug("Card with id {} is authenticated", cardId);
    }
}
