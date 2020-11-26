package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.domain.entities.Player;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    @Query("SELECT p FROM Player as p WHERE p.team.name = :name ORDER BY p.id")
    Set<Player> exportPlayersInNorthHub(String name);

    Set<Player> findAllBySalaryGreaterThanOrderBySalaryDesc(BigDecimal num);
}
