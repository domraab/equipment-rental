package cz.fei.equipmentrental.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal dailyRate;

    public Equipment() {}

    public Equipment(String name, BigDecimal dailyRate) {
        this.name = name;
        this.dailyRate = dailyRate;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }
}
