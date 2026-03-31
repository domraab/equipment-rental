package cz.fei.equipmentrental.test;

import cz.fei.equipmentrental.entity.Equipment;
import cz.fei.equipmentrental.entity.User;
import cz.fei.equipmentrental.repository.EquipmentRepository;
import cz.fei.equipmentrental.repository.RentalRepository;
import cz.fei.equipmentrental.repository.UserRepository;
import cz.fei.equipmentrental.service.RentalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class RentalServiceTest {

    @Test
    void shouldThrowExceptionWhenEndDateIsBeforeStartDate() {
        RentalService rentalService = new RentalService(null, null, null);

        LocalDate startDate = LocalDate.of(2023, 10, 5);
        LocalDate endDate = LocalDate.of(2023, 10, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            rentalService.createRental(1L, 1L, startDate, endDate);
        });
    }

    @Test
    void shouldThrowExceptionWhenUserHasMoreThanThreeActiveRentals() {
        RentalRepository mockRentalRepo = Mockito.mock(RentalRepository.class);

        when(mockRentalRepo.countActiveRentalsByUserId(1L)).thenReturn(3L);

        RentalService rentalService = new RentalService(mockRentalRepo, null, null);

        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = LocalDate.of(2023, 10, 5);

        assertThrows(IllegalStateException.class, () -> {
            rentalService.createRental(1L, 1L, startDate, endDate);
        });
    }

    @Test
    void shoudlThrowExceptionWhenEquipmentIsAlreadyRentedInGivenPeriod() {
        RentalRepository mockRentalRepo = Mockito.mock(RentalRepository.class);

        LocalDate startDate = LocalDate.of(2023, 10, 10);
        LocalDate endDate = LocalDate.of(2023, 10, 15);
        Long equipmentId = 5L;

        when(mockRentalRepo.isEquipmentAvailable(equipmentId, startDate, endDate)).thenReturn(false);

        RentalService rentalService = new RentalService(mockRentalRepo, null, null);

        assertThrows(IllegalStateException.class, () -> {
            rentalService.createRental(1L, equipmentId, startDate, endDate);
        });
    }

    @Test
    void shouldApplyTenPercentDiscountForRentalsSevenDaysOrLonger() {
        RentalRepository mockRentalRepo = Mockito.mock(RentalRepository.class);
        UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
        EquipmentRepository mockEquipmentRepo = Mockito.mock(EquipmentRepository.class);

        Long equipmentId = 2L;
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = LocalDate.of(2023, 10, 11);

        when(mockRentalRepo.isEquipmentAvailable(equipmentId, startDate, endDate)).thenReturn(true);
        when(mockRentalRepo.countActiveRentalsByUserId(userId)).thenReturn(0L);
        when(mockRentalRepo.getDailyRate(equipmentId)).thenReturn(new BigDecimal("100.00"));

        User mockUser = Mockito.mock(User.class);
        Equipment mockEquipment = Mockito.mock(Equipment.class);
        when(mockUserRepo.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockEquipmentRepo.findById(equipmentId)).thenReturn(Optional.of(mockEquipment));

        RentalService rentalService = new RentalService(mockRentalRepo, mockUserRepo, mockEquipmentRepo);

        BigDecimal totalPrice = rentalService.createRental(userId, equipmentId, startDate, endDate);

        assertEquals(new BigDecimal("900.00"), totalPrice);
    }
}