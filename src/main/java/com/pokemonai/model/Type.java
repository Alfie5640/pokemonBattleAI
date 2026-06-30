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

        // WATER
        chart.get(WATER).put(FIRE, 2.0);
        chart.get(WATER).put(GROUND, 2.0);
        chart.get(WATER).put(ROCK, 2.0);
        chart.get(WATER).put(WATER, 0.5);
        chart.get(WATER).put(GRASS, 0.5);
        chart.get(WATER).put(DRAGON, 0.5);

        // ELECTRIC
        chart.get(ELECTRIC).put(WATER, 2.0);
        chart.get(ELECTRIC).put(FLYING, 2.0);
        chart.get(ELECTRIC).put(ELECTRIC, 0.5);
        chart.get(ELECTRIC).put(GRASS, 0.5);
        chart.get(ELECTRIC).put(DRAGON, 0.5);
        chart.get(ELECTRIC).put(GROUND, 0.0);

        // GRASS
        chart.get(GRASS).put(WATER, 2.0);
        chart.get(GRASS).put(GROUND, 2.0);
        chart.get(GRASS).put(ROCK, 2.0);
        chart.get(GRASS).put(FIRE, 0.5);
        chart.get(GRASS).put(GRASS, 0.5);
        chart.get(GRASS).put(POISON, 0.5);
        chart.get(GRASS).put(FLYING, 0.5);
        chart.get(GRASS).put(BUG, 0.5);
        chart.get(GRASS).put(DRAGON, 0.5);
        chart.get(GRASS).put(STEEL, 0.5);

        // ICE
        chart.get(ICE).put(GRASS, 2.0);
        chart.get(ICE).put(GROUND, 2.0);
        chart.get(ICE).put(FLYING, 2.0);
        chart.get(ICE).put(DRAGON, 2.0);
        chart.get(ICE).put(FIRE, 0.5);
        chart.get(ICE).put(WATER, 0.5);
        chart.get(ICE).put(ICE, 0.5);
        chart.get(ICE).put(STEEL, 0.5);

        // FIGHTING
        chart.get(FIGHTING).put(NORMAL, 2.0);
        chart.get(FIGHTING).put(ICE, 2.0);
        chart.get(FIGHTING).put(ROCK, 2.0);
        chart.get(FIGHTING).put(DARK, 2.0);
        chart.get(FIGHTING).put(STEEL, 2.0);
        chart.get(FIGHTING).put(POISON, 0.5);
        chart.get(FIGHTING).put(FLYING, 0.5);
        chart.get(FIGHTING).put(PSYCHIC, 0.5);
        chart.get(FIGHTING).put(BUG, 0.5);
        chart.get(FIGHTING).put(FAIRY, 0.5);
        chart.get(FIGHTING).put(GHOST, 0.0);

        // POISON
        chart.get(POISON).put(GRASS, 2.0);
        chart.get(POISON).put(FAIRY, 2.0);
        chart.get(POISON).put(POISON, 0.5);
        chart.get(POISON).put(GROUND, 0.5);
        chart.get(POISON).put(ROCK, 0.5);
        chart.get(POISON).put(GHOST, 0.5);
        chart.get(POISON).put(STEEL, 0.0);

        // GROUND
        chart.get(GROUND).put(FIRE, 2.0);
        chart.get(GROUND).put(ELECTRIC, 2.0);
        chart.get(GROUND).put(POISON, 2.0);
        chart.get(GROUND).put(ROCK, 2.0);
        chart.get(GROUND).put(STEEL, 2.0);
        chart.get(GROUND).put(GRASS, 0.5);
        chart.get(GROUND).put(BUG, 0.5);
        chart.get(GROUND).put(FLYING, 0.0);

        // FLYING
        chart.get(FLYING).put(GRASS, 2.0);
        chart.get(FLYING).put(FIGHTING, 2.0);
        chart.get(FLYING).put(BUG, 2.0);
        chart.get(FLYING).put(ELECTRIC, 0.5);
        chart.get(FLYING).put(ROCK, 0.5);
        chart.get(FLYING).put(STEEL, 0.5);

        // PSYCHIC
        chart.get(PSYCHIC).put(FIGHTING, 2.0);
        chart.get(PSYCHIC).put(POISON, 2.0);
        chart.get(PSYCHIC).put(PSYCHIC, 0.5);
        chart.get(PSYCHIC).put(STEEL, 0.5);
        chart.get(PSYCHIC).put(DARK, 0.0);

        // BUG
        chart.get(BUG).put(GRASS, 2.0);
        chart.get(BUG).put(PSYCHIC, 2.0);
        chart.get(BUG).put(DARK, 2.0);
        chart.get(BUG).put(FIRE, 0.5);
        chart.get(BUG).put(FIGHTING, 0.5);
        chart.get(BUG).put(POISON, 0.5);
        chart.get(BUG).put(FLYING, 0.5);
        chart.get(BUG).put(GHOST, 0.5);
        chart.get(BUG).put(STEEL, 0.5);
        chart.get(BUG).put(FAIRY, 0.5);

        // ROCK
        chart.get(ROCK).put(FIRE, 2.0);
        chart.get(ROCK).put(ICE, 2.0);
        chart.get(ROCK).put(FLYING, 2.0);
        chart.get(ROCK).put(BUG, 2.0);
        chart.get(ROCK).put(FIGHTING, 0.5);
        chart.get(ROCK).put(GROUND, 0.5);
        chart.get(ROCK).put(STEEL, 0.5);

        // GHOST
        chart.get(GHOST).put(PSYCHIC, 2.0);
        chart.get(GHOST).put(GHOST, 2.0);
        chart.get(GHOST).put(DARK, 0.5);
        chart.get(GHOST).put(NORMAL, 0.0);

        // DRAGON
        chart.get(DRAGON).put(DRAGON, 2.0);
        chart.get(DRAGON).put(STEEL, 0.5);
        chart.get(DRAGON).put(FAIRY, 0.0);

        // DARK
        chart.get(DARK).put(PSYCHIC, 2.0);
        chart.get(DARK).put(GHOST, 2.0);
        chart.get(DARK).put(FIGHTING, 0.5);
        chart.get(DARK).put(DARK, 0.5);
        chart.get(DARK).put(FAIRY, 0.5);

        // STEEL
        chart.get(STEEL).put(ICE, 2.0);
        chart.get(STEEL).put(ROCK, 2.0);
        chart.get(STEEL).put(FAIRY, 2.0);
        chart.get(STEEL).put(FIRE, 0.5);
        chart.get(STEEL).put(WATER, 0.5);
        chart.get(STEEL).put(ELECTRIC, 0.5);
        chart.get(STEEL).put(STEEL, 0.5);

        // FAIRY
        chart.get(FAIRY).put(FIGHTING, 2.0);
        chart.get(FAIRY).put(DRAGON, 2.0);
        chart.get(FAIRY).put(DARK, 2.0);
        chart.get(FAIRY).put(FIRE, 0.5);
        chart.get(FAIRY).put(POISON, 0.5);
        chart.get(FAIRY).put(STEEL, 0.5);
    }

    public double calculateEffectiveness(Type opponent) {
        return chart.get(this).getOrDefault(opponent, 1.0);
    }
}
