package cz.fei.equipmentrental.test;

import cz.fei.equipmentrental.repository.RentalRepository;
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

    @Test
    void shoudlThrowExceptionWhenEquipmentIsAlreadyRentedInGivenPeriod() {
        RentalRepository mockRepository = Mockito.mock(RentalRepository.class);

        LocalDate startDate = LocalDate.of(2023, 10, 10);
        LocalDate endDate = LocalDate.of(2023, 10, 15);
        Long equipmentId = 5L;

        when(mockRepository.isEquipmentAvailable(equipmentId, startDate, endDate)).thenReturn(false);;

        RentalService rentalService = new RentalService(mockRepository);

        assertThrows(IllegalStateException.class, () -> {
            rentalService.createRental(1L, equipmentId, startDate, endDate);
        });
    }

    @Test
    void shouldApplyTenPercentDiscountForRentalsSevenDaysOrLonger() {
        RentalRepository mockRepository = Mockito.mock(RentalRepository.class);

        Long equipmentId = 2L;
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = LocalDate.of(2023, 10, 11);

        when(mockRepository.isEquipmentAvailable(equipmentId, startDate, endDate)).thenReturn(true);
        when(mockRepository.countActiveRentalsByUserId(1L)).thenReturn(0L);
        when(mockRepository.getDailyRate(equipmentId)).thenReturn(new java.math.BigDecimal("100.00"));

        RentalService rentalService = new RentalService(mockRepository);

        java.math.BigDecimal totalPrice = rentalService.createRental(1l, equipmentId, startDate, endDate);

        org.junit.jupiter.api.Assertions.assertEquals(new java.math.BigDecimal("900.00"), totalPrice);
    }
}
