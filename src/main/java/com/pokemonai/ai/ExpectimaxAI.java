package com.pokemonai.ai;

import com.pokemonai.model.BattleState;
import com.pokemonai.model.Move;

import java.util.List;

// RNG includes move accuracy, crit rolls, damage variance
public class ExpectimaxAI implements AIStrategy {

    @Override
    public Move makeDecision(BattleState state, List<Move> moveList) {
        Move bestMove = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        int depth = 3;

        for (Move move : moveList) {
            double hitProb  = (double) move.accuracy() / 100.0;
            double missProb = 1 - hitProb;

            double hitScore  = expectimax(state.applyMoveForSearch(move), depth - 1, false);
            double missScore = expectimax(state,                 depth - 1, false);

            double chanceScore = (hitProb * hitScore) + (missProb * missScore);

            if (chanceScore > bestScore) {
                bestScore = chanceScore;
                bestMove  = move;
            }
        }
        return bestMove;
    }

    private double expectimax(BattleState state, int depth, boolean isMaximizing) {
        if (depth == 0 || state.getTrainerPokemon().isFainted() || state.getOpponentPokemon().isFainted()) {
            return Heuristic.evaluateState(state);
        }

        List<Move> moveList;
        if (isMaximizing) {
            double best = Double.NEGATIVE_INFINITY;
            moveList = state.getTrainerPokemon().getMoveList();
            for (Move move : moveList) {
                double hitProb  = (double) move.accuracy() / 100.0;
                double missProb = 1 - hitProb;

                double hitScore  = expectimax(state.applyMoveForSearch(move), depth - 1, false);
                double missScore = expectimax(state,                 depth - 1, false);

                double chanceScore = (hitProb * hitScore) + (missProb * missScore);
                best = Math.max(best, chanceScore);
            }
            return best;
        } else {
            double best = Double.POSITIVE_INFINITY;
            moveList = state.getOpponentPokemon().getMoveList();
            for (Move move : moveList) {
                double hitProb  = (double) move.accuracy() / 100.0;
                double missProb = 1 - hitProb;

                double hitScore  = expectimax(state.applyMoveForSearch(move), depth - 1, true);
                double missScore = expectimax(state,                 depth - 1, true);

                double chanceScore = (hitProb * hitScore) + (missProb * missScore);
                best = Math.min(best, chanceScore);
            }
            return best;
        }
    }
}