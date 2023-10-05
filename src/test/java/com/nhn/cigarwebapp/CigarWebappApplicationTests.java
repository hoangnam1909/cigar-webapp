package com.nhn.cigarwebapp;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
