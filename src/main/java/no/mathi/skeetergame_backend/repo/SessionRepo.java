package no.mathi.skeetergame_backend.repo;

import no.mathi.skeetergame_backend.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepo extends JpaRepository<Session, Long> {
}
