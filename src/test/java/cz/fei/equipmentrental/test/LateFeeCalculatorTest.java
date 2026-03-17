package cz.fei.equipmentrental.test;

import cz.fei.equipmentrental.domain.LateFeeCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LateFeeCalculatorTest {

    @Test
    void shouldCalculateLateFeeAsDoubleOfDailyRatePerLateDay() {
        LateFeeCalculator calculator = new LateFeeCalculator();
        int daysLate = 3;
        BigDecimal dailyRate = new BigDecimal("250.00");

        BigDecimal lateFee = calculator.calculate(daysLate, dailyRate);

        assertEquals(new BigDecimal("1500.00"), lateFee);
    }
}
