package com.tw.Entities;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class RandomNumberGeneratorTest {
    @Test
    void should_generate_8_random_number() {
        String firstNumber = RandomNumberGenerator.generate8BitRandomNumber();
        String secondNumber = RandomNumberGenerator.generate8BitRandomNumber();

        assertThat(Objects.requireNonNull(firstNumber).length()).isEqualTo(8);
        assertThat(Objects.requireNonNull(secondNumber).length()).isEqualTo(8);
        assertNotSame(firstNumber, secondNumber);
    }
}