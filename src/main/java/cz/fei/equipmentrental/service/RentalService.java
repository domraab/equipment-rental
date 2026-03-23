package cz.fei.equipmentrental.service;

import java.time.LocalDate;

public class RentalService {
    public void createRental(Long userId, Long equipmnetID, LocalDate startDate, LocalDate endDate){
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Datum konce vypůjčení nesmí být před začátkem");
        }
    }
}
