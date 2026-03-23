package cz.fei.equipmentrental.test;

import cz.fei.equipmentrental.service.RentalService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RentalServiceTest {

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        RentalService rentalService = new RentalService();

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate endDate = LocalDate.of(2023, 10, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            rentalService.createRental(1L, 1L, startDate, endDate);
        });
    }
}
