package cz.fei.equipmentrental.repository;

import java.time.LocalDate;

public interface RentalRepository {
    long countActiveRentalsByUserId(Long userID);

    boolean isEquipmentAvailable(Long equipmentId, LocalDate startDate, LocalDate endDate);

    java.math.BigDecimal getDailyRate(Long equipmentId);

}
