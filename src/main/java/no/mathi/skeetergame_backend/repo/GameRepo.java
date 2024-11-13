package no.mathi.skeetergame_backend.repo;

import no.mathi.skeetergame_backend.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepo extends JpaRepository<Game, Long> {
}
