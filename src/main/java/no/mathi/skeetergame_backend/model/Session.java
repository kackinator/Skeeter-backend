package no.mathi.skeetergame_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "host_id", nullable = false)
    private User host;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    private boolean open;

    @OneToMany
    @JoinColumn(name = "session_id")
    private List<User> players = new ArrayList<>();

    // Optional - add created and updated timestamps
    private Long createdAt;
    private Long updatedAt;

    // Constructors, getters, setters

    public Session() {}

    public Session(User host, Game game) {
        this.host = host;
        this.game = game;
        this.open = true;
        this.createdAt = System.currentTimeMillis();
    }

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}
