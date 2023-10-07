package com.nhn.cigarwebapp;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
class CigarWebappApplicationTests {

    @Test
    void isShouldAddNumbers() {
        int numberOne = 10;
        int numberTwo = 20;

        int result = 40;

        int expected = 30;
        assertThat(result).isEqualTo(expected);
    }

}
