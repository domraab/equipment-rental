package cz.fei.equipmentrental.service;

import cz.fei.equipmentrental.domain.RentalRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalService {

    private RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public RentalService() {
    }

    public BigDecimal createRental(Long userId, Long equipmentId, LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);

        if (rentalRepository != null) {
            checkUserRentalLimits(userId);
            checkEquipmentAvailability(equipmentId, startDate, endDate);
            return calculateTotalPrice(equipmentId, startDate, endDate);
        }

        return BigDecimal.ZERO;
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Datum konce vypůjčení nesmí být před začátkem");
        }
    }

    private void checkUserRentalLimits(Long userId) {
        long activeRentals = rentalRepository.countActiveRentalsByUserId(userId);
        if (activeRentals >= 3 ) {
            throw new IllegalStateException("Zákazník nesmí mít více než 3 aktivní výpůjčky.");
        }
    }

    private void checkEquipmentAvailability(Long equipmentId, LocalDate startDate, LocalDate endDate) {
        boolean isAvailable = rentalRepository.isEquipmentAvailable(equipmentId, startDate, endDate);
        if (!isAvailable) {
            throw new IllegalStateException("Vybavení je v tomto termínu již půjčené.");
        }
    }

    private BigDecimal calculateTotalPrice(Long equipmentId, LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal dailyRate = rentalRepository.getDailyRate(equipmentId);

        if (dailyRate == null) {
            dailyRate = BigDecimal.ZERO;
        }

        BigDecimal totalPrice = dailyRate.multiply(BigDecimal.valueOf(days));

        if (days >= 7) {
            totalPrice = totalPrice.multiply(new BigDecimal("0.90"));
        }

        return totalPrice.setScale(2, RoundingMode.HALF_UP);
    }
}