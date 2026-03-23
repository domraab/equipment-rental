package cz.fei.equipmentrental.domain;

public interface RentalRepository {
    long countActiveRentalsByUserId(Long userID);
}
