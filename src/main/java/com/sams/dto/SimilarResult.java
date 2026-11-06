package com.sams.dto;

public class SimilarResult {
    private Long playerId;
    private String name;
    private String position;
    private double marketValue;
    private double similarity;

    public SimilarResult(Long playerId, String name, String position,
                         double marketValue, double similarity) {
        this.playerId = playerId; this.name = name; this.position = position;
        this.marketValue = marketValue; this.similarity = similarity;
    }

    public Long getPlayerId() { return playerId; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public double getMarketValue() { return marketValue; }
    public double getSimilarity() { return similarity; }
}