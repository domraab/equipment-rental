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

    public void createRental(Long userId, Long equipmentId, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Datum konce vypůjčení nesmí být před začátkem");
        }
    }
}