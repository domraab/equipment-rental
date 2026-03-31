package cz.fei.equipmentrental.service;

import cz.fei.equipmentrental.entity.Equipment;
import cz.fei.equipmentrental.entity.Rental;
import cz.fei.equipmentrental.entity.User;
import cz.fei.equipmentrental.repository.EquipmentRepository;
import cz.fei.equipmentrental.repository.RentalRepository;
import cz.fei.equipmentrental.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class RentalService {


    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;


    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, EquipmentRepository equipmentRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public BigDecimal createRental(Long userId, Long equipmentId, LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);


        checkUserRentalLimits(userId);
        checkEquipmentAvailability(equipmentId, startDate, endDate);
        BigDecimal totalPrice = calculateTotalPrice(equipmentId, startDate, endDate);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Uživatel nenalezen"));
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Vybavení nenalezeno"));

        Rental rental = new Rental(user, equipment, startDate, endDate, totalPrice);
        rentalRepository.save(rental);

        return totalPrice;
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