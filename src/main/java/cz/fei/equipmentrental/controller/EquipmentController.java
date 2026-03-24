package cz.fei.equipmentrental.controller;


import cz.fei.equipmentrental.entity.Equipment;
import cz.fei.equipmentrental.repository.EquipmentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;

    public EquipmentController(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @GetMapping
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    // Dočasná pomocná adresa pro snadné vytvoření testovacích dat přes prohlížeč
    @GetMapping("/pridaj-test")
    public String addTestEquipment() {
        Equipment bager = new Equipment();
        bager.setName("Testovací bagr CAT");
        bager.setDailyRate(new BigDecimal("1500.00"));

        equipmentRepository.save(bager);

        return "Sláva! Testovací bagr byl úspěšně uložen do databáze.";
    }
}
