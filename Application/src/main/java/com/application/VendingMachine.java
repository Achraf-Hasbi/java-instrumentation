package com.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VendingMachine {
    private static Logger LOGGER = LoggerFactory.getLogger(VendingMachine.class);

    public static void buy(int itemId) {
        try {
            Thread.sleep((long)(Math.random() * 2000));
            LOGGER.info("[Application] Successfully purchased item : {} ", itemId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}