package cz.fei.equipmentrental.repository;

import cz.fei.equipmentrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}