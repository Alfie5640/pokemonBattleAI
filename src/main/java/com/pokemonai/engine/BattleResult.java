package com.pokemonai.engine;

public record BattleResult(Outcome outcome, int turnCount) {

    public enum Outcome {
        TRAINER_WON,
        OPPONENT_WON,
        DRAW
    }

    public boolean trainerWon() {
        return outcome == Outcome.TRAINER_WON;
    }

    public boolean isDraw() {
        return outcome == Outcome.DRAW;
    }
}