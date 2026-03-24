package cz.fei.equipmentrental.repository;

import cz.fei.equipmentrental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT COUNT(r) FROM Rental r WHERE r.user.id = :userId AND r.endDate >= CURRENT_DATE")
    long countActiveRentalsByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN false ELSE true END FROM Rental r WHERE r.equipment.id = :equipmentId AND (r.startDate <= :endDate AND r.endDate >= :startDate)")
    boolean isEquipmentAvailable(@Param("equipmentId") Long equipmentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT e.dailyRate FROM Equipment e WHERE e.id = :equipmentId")
    BigDecimal getDailyRate(@Param("equipmentId") Long equipmentId);
}
