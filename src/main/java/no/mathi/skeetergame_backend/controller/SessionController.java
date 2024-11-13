package no.mathi.skeetergame_backend.controller;

import no.mathi.skeetergame_backend.model.Session;
import no.mathi.skeetergame_backend.repo.SessionRepo;
import no.mathi.skeetergame_backend.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    //Create a new session
    @PostMapping("/new/{username}/{gameId}")
    public Session createSession(@PathVariable String username, @PathVariable Long gameId) {
        return sessionService.createSession(username, gameId);
    }

    //Join a session
    @PostMapping("/join/{username}/{sessionId}")
    public Session joinSession(@PathVariable String username, @PathVariable Long sessionId) {
        sessionService.joinSession(username, sessionId);
        return sessionService.getSession(sessionId);
    }

    //Get all open sessions
    @GetMapping("/open")
    public Iterable<Session> getOpenSessions() {
        return sessionService.getOpenSessions();
    }

    //Get an active session by game id
    @GetMapping("/active/{gameId}")
    public Session getActiveSession(@PathVariable Long gameId) {
        return sessionService.getActiveSession(gameId);
    }

    //Get an active session by game name
    @GetMapping("/active/name/{gameName}")
    public Session getActiveSession(@PathVariable String gameName) {
        return sessionService.getActiveSessionByName(gameName);
    }

}
