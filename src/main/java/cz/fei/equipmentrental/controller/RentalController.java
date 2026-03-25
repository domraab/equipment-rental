package cz.fei.equipmentrental.controller;

import cz.fei.equipmentrental.dto.RentalRequestDTO;
import cz.fei.equipmentrental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;


    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<?> createRental(@Valid @RequestBody RentalRequestDTO request) {
        BigDecimal totalPrice = rentalService.createRental(
                request.getUserId(),
                request.getEquipmentId(),
                request.getStartDate(),
                request.getEndDate()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Výpůjčka byla úspěšně vytvořena.",
                        "totalPrice", totalPrice
                ));
    }
}