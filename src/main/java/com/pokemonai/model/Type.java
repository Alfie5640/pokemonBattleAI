package com.pokemonai.model;

import java.util.EnumMap;
import java.util.Map;

// Enum of all Pokemon types, handles effectiveness multipliers
public enum Type {
    NORMAL,
    FIRE,
    WATER,
    ELECTRIC,
    GRASS,
    ICE,
    FIGHTING,
    POISON,
    GROUND,
    FLYING,
    PSYCHIC,
    BUG,
    ROCK,
    GHOST,
    DRAGON,
    DARK,
    STEEL,
    FAIRY;

    private static final Map<Type, Map<Type, Double>> chart = new EnumMap<>(Type.class);
    static {
        for (Type type : Type.values()) {
            chart.put(type, new EnumMap<>(Type.class));
        }

        // NORMAL
        chart.get(NORMAL).put(ROCK, 0.5);
        chart.get(NORMAL).put(STEEL, 0.5);
        chart.get(NORMAL).put(GHOST, 0.0);

        // FIRE
        chart.get(FIRE).put(GRASS, 2.0);
        chart.get(FIRE).put(ICE, 2.0);
        chart.get(FIRE).put(BUG, 2.0);
        chart.get(FIRE).put(STEEL, 2.0);

        chart.get(FIRE).put(FIRE, 0.5);
        chart.get(FIRE).put(WATER, 0.5);
        chart.get(FIRE).put(ROCK, 0.5);
        chart.get(FIRE).put(DRAGON, 0.5);

        //WATER

    }

    public double calculateEffectiveness(Type opponent) {
        return chart.get(this).getOrDefault(opponent, 1.0);
    }
}
