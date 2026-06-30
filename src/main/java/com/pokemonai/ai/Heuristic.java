package com.pokemonai.ai;

import com.pokemonai.model.BattleState;

// Scores a BattleState as a single number.
public class Heuristic {
    public static int evaluateState(BattleState currentState) {
        double oppHP = ((double)currentState.getOpponentPokemon().getCurrentHP()) / (currentState.getOpponentPokemon().getBaseStats().hp()) * 100 ;
        double trainerHP = ((double)currentState.getTrainerPokemon().getCurrentHP()) / (currentState.getTrainerPokemon().getBaseStats().hp()) * 100;

        return (int)((trainerHP - oppHP) * 5);
    }

    private double normalizeValue(double value, double min, double max) {
        return ((value - min) / (max - min));
    }
}
