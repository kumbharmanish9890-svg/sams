package com.sams.model;

import jakarta.persistence.*;

@Entity
public class ScoutProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private int pace;
    private int shooting;
    private int passing;
    private int dribbling;
    private int defending;
    private int physical;
    private double marketValue; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public int getPace() { return pace; }
    public void setPace(int pace) { this.pace = pace; }
    public int getShooting() { return shooting; }
    public void setShooting(int shooting) { this.shooting = shooting; }
    public int getPassing() { return passing; }
    public void setPassing(int passing) { this.passing = passing; }
    public int getDribbling() { return dribbling; }
    public void setDribbling(int dribbling) { this.dribbling = dribbling; }
    public int getDefending() { return defending; }
    public void setDefending(int defending) { this.defending = defending; }
    public int getPhysical() { return physical; }
    public void setPhysical(int physical) { this.physical = physical; }
    public double getMarketValue() { return marketValue; }
    public void setMarketValue(double marketValue) { this.marketValue = marketValue; }
}