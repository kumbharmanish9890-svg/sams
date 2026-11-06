package com.sams.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
public class TrainingLoad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private LocalDate date;
    private int rpe;          
    private int durationMin;  

    public int getLoad() { return rpe * durationMin; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getRpe() { return rpe; }
    public void setRpe(int rpe) { this.rpe = rpe; }
    public int getDurationMin() { return durationMin; }
    public void setDurationMin(int durationMin) { this.durationMin = durationMin; }
}