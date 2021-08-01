package com.playtomic.tests.wallet.helper;

import com.playtomic.tests.wallet.exception.BadRequestException;

public interface ValidationHelper {
    String ILLEGAL = "Invalid property:%s, value:%s";

    /**
     * Helper method to check positive values
     * @param name field
     * @param id id value
     */
    static void checkPositive(final String name, final long id) {
        if (id < 1L) {
            throw new BadRequestException(String.format(ILLEGAL, name, id));
        }
    }
}
