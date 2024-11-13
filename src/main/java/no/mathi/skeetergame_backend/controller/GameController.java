package no.mathi.skeetergame_backend.controller;

import lombok.Getter;
import no.mathi.skeetergame_backend.model.Game;
import no.mathi.skeetergame_backend.repo.GameRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameRepo gameRepository;

    @PostMapping
    public Game createGame(@RequestBody Game game) {
        return gameRepository.save(game);
    }

    @GetMapping("/all")
    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable Long id) {
        return gameRepository.findById(id).orElse(null);
    }
}

