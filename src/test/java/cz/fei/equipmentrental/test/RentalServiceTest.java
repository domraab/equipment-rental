package cz.fei.equipmentrental.test;

import cz.fei.equipmentrental.domain.RentalRepository;
import cz.fei.equipmentrental.service.RentalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldThrowExceptionWhenUserHasMoreThanThreeActiveRentals() {
        RentalRepository mockRepository = Mockito.mock(RentalRepository.class);

        when(mockRepository.countActiveRentalsByUserId(1L)).thenReturn(3L);

        RentalService rentalService = new RentalService(mockRepository);

        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = LocalDate.of(2023, 10, 5);

        assertThrows(IllegalStateException.class, () -> {
            rentalService.createRental(1L, 1L, startDate, endDate);
        });
    }
}
