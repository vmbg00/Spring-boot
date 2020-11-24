package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Passenger;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {

    Optional<Passenger> findByEmail(String email);

    @Query("SELECT p FROM Passenger as p ORDER BY p.tickets.size DESC, p.email ASC")
    Set<Passenger> exportPassengers();
}
