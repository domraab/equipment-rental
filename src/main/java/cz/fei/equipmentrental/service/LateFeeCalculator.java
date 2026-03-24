package cz.fei.equipmentrental.service;

import java.math.BigDecimal;

public class LateFeeCalculator {
    public BigDecimal calculate(int daysLate, BigDecimal dailyRate) {

        BigDecimal penaltyRate = dailyRate.multiply(BigDecimal.valueOf(2));

        return penaltyRate.multiply(new BigDecimal(daysLate));
    }
}
