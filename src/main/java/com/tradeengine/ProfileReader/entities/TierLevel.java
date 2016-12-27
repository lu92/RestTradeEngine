package com.tradeengine.ProfileReader.entities;

public enum TierLevel
{
    GOLD(1),
    SILVER(2),
    PREMIUM(3),
    STANDARD(4);

    private int weight;

    TierLevel(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
