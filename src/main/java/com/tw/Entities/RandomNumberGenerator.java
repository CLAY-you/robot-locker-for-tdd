package com.tw.Entities;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RandomNumberGenerator {
    public static String generate8BitRandomNumber() {
        BigDecimal random = BigDecimal.valueOf(Math.random() * 100000000);
        BigInteger bigInteger = random.toBigInteger();
        int generatedNumberLength = bigInteger.toString().length();
        if (generatedNumberLength < 8) {
            return bigInteger + ("0".repeat(generatedNumberLength));
        }
        return bigInteger.toString();
    }
}
