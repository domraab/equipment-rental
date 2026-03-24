package cz.fei.equipmentrental.repository;

import cz.fei.equipmentrental.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
