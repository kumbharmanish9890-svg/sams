package com.sams.dto;

public class PlayerRisk {
    private Long playerId;
    private String name;
    private double acute;
    private double chronic;
    private double acwr;
    private String status;
    private String badge;

    public PlayerRisk(Long playerId, String name, double acute, double chronic,
                      double acwr, String status, String badge) {
        this.playerId = playerId; this.name = name; this.acute = acute;
        this.chronic = chronic; this.acwr = acwr; this.status = status; this.badge = badge;
    }

    public Long getPlayerId() { return playerId; }
    public String getName() { return name; }
    public double getAcute() { return acute; }
    public double getChronic() { return chronic; }
    public double getAcwr() { return acwr; }
    public String getStatus() { return status; }
    public String getBadge() { return badge; }
}