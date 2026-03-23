package cz.fei.equipmentrental.service;

import cz.fei.equipmentrental.domain.RentalRepository;
import java.time.LocalDate;

public class RentalService {

    private RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public RentalService() {
    }

    public java.math.BigDecimal createRental(Long userId, Long equipmentId, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Datum konce vypůjčení nesmí být před začátkem");
        }

        if (rentalRepository != null) {

            long activeRentals = rentalRepository.countActiveRentalsByUserId(userId);
            if (activeRentals >= 3 ) {
                throw new IllegalStateException("Zákazník nesmí mít více než 3 aktivní výpůjčky.");
            }

            boolean isAvailable = rentalRepository.isEquipmentAvailable(equipmentId, startDate, endDate);
            if (!isAvailable) {
                throw new IllegalStateException("Vybavení je v tomto termínu již půjčené.");
            }
        }

        return java.math.BigDecimal.ZERO;
    }
}