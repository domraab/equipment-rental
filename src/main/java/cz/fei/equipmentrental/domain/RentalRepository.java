package cz.fei.equipmentrental.domain;

import java.time.LocalDate;

public interface RentalRepository {
    long countActiveRentalsByUserId(Long userID);

    boolean isEquipmentAvailable(Long equipmentId, LocalDate startDate, LocalDate endDate);
}
