package cz.fei.equipmentrental.domain;

import java.math.BigDecimal;

public class LateFeeCalculator {
    public BigDecimal calculate(int daysLate, BigDecimal dailyRate) {

        BigDecimal penaltyRate = dailyRate.multiply(new BigDecimal("2"));

        return penaltyRate.multiply(new BigDecimal(daysLate));
    }
}
