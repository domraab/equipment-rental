package cz.fei.equipmentrental.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class RentalRequestDTO {

    @NotNull(message = "ID uživatele je povinné.")
    private Long userId;

    @NotNull(message = "ID vybavení je povinné.")
    private Long equipmentId;

    @NotNull(message = "Datum začátku je povinné.")
    @FutureOrPresent(message = "Datum začátku nesmí být v minulosti.")
    private LocalDate startDate;

    @NotNull(message = "Datum konce je povinné.")
    private LocalDate endDate;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getEquipmentId() { return equipmentId; }
    public void setEquipmentId(Long equipmentId) { this.equipmentId = equipmentId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
}