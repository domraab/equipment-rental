package cz.fei.equipmentrental.test;


import cz.fei.equipmentrental.entity.Equipment;
import cz.fei.equipmentrental.repository.EquipmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    void shouldSaveAndRetrieveEquipment() {
        Equipment excavator = new Equipment("Bagr", new BigDecimal("1500.00"));

        Equipment savedEquipment = equipmentRepository.save(excavator);

        Optional<Equipment> foundEquipment = equipmentRepository.findById(savedEquipment.getId());

        assertTrue(foundEquipment.isPresent());
        assertEquals("Bagr", foundEquipment.get().getName());
    }
}
