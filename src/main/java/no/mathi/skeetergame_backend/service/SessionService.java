package no.mathi.skeetergame_backend.service;

import no.mathi.skeetergame_backend.model.Game;
import no.mathi.skeetergame_backend.model.Session;
import no.mathi.skeetergame_backend.model.User;
import no.mathi.skeetergame_backend.repo.GameRepo;
import no.mathi.skeetergame_backend.repo.SessionRepo;
import no.mathi.skeetergame_backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SessionService {

    @Autowired
    private SessionRepo sessionRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private GameRepo gameRepository;

    //Create a new session
    public Session createSession(String username, Long gameId){
        User user = userRepository.findByUsername(username).orElse(null);
        Game game = gameRepository.findById(gameId).orElse(null);

        Session session = new Session();

        session.setHost(user);
        session.setGame(game);
        session.setOpen(true);
        return sessionRepository.save(session);
    }

    //Join a session
    public void joinSession(String username, Long sessionId){
        User user = userRepository.findByUsername(username).orElse(null);
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if(session == null || user == null){
            return;
        }

        session.getPlayers().add(user);
        sessionRepository.save(session);
    }

    //Leave a session
    public void leaveSession(String username, Long sessionId) {
        User user = userRepository.findByUsername(username).orElse(null);
        Session session = sessionRepository.findById(sessionId).orElse(null);

        if (session == null || user == null) {
            return;
        }

        session.getPlayers().remove(user);
        sessionRepository.save(session);
    }

    //Get a session
    public Session getSession(Long sessionId){
        return sessionRepository.findById(sessionId).orElse(null);
    }

    //Get session by id
    public Session getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId).orElse(null);
    }

    public Iterable<Session> getOpenSessions() {
        //Get all sessions
        Iterable<Session> sessions = sessionRepository.findAll();

        //Filter out the closed sessions
        return StreamSupport.stream(sessions.spliterator(), false)
                .filter(Session::isOpen)
                .collect(Collectors.toList());
    }

    public Session getActiveSession(Long gameId) {
        //Get all sessions
        Iterable<Session> sessions = sessionRepository.findAll();

        //Filter out the closed sessions
        return StreamSupport.stream(sessions.spliterator(), false)
                .filter(session -> session.isOpen() && session.getGame().getId().equals(gameId))
                .findFirst()
                .orElse(null);
    }

    public Session getActiveSessionByName(String gameName) {
        List<Game> games = gameRepository.findAll();

        for(Game game : games){
            System.out.println(game.getName());
            if(game.getName().equals(gameName)){
                Session session = getActiveSession(game.getId());
                if(session != null){
                    return session;
                }
            }else{

            }
        }
        return null;
    }
}
