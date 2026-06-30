package com.pokemonai.ai;

import com.pokemonai.model.BattleState;
import com.pokemonai.model.Move;

import java.util.List;

// Implements AIStrategy. Searches game tree assuming perfect opponent. (alpha beta pruning also)
// Maybe a hashmap of score to move? or something
public class MinimaxAI implements AIStrategy{

    @Override
    public Move makeDecision(BattleState state, List<Move> moveList) {
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        int depth = 3;
        boolean isMaximizing = true;

        for (int i = 0; i < moveList.size(); i++) {
            BattleState nextState = state.applyMove(moveList.get(i));
            int score = minimax(nextState, depth, !(isMaximizing));

            if (score > bestScore) {
                bestScore = score;
                bestMove = moveList.get(i);
            }
        }
        return bestMove;
    }

    private int minimax(BattleState state, int depth, boolean isMaximizing) {
        if (depth == 0 || state.getTrainerPokemon().isFainted() || state.getOpponentPokemon().isFainted()) {
            return Heuristic.evaluateState(state);
        }

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            List<Move> moveList = state.getTrainerPokemon().getMoveList();
            for (Move move : moveList) {
                int score = minimax(state.applyMove(move), depth - 1, false);
                best = Math.max(best, score);
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            List<Move> moveList = state.getOpponentPokemon().getMoveList();
            for (Move move : moveList) {
                int score = minimax(state.applyMove(move), depth - 1, true);
                best = Math.min(best, score);
            }
            return best;
        }
    }

}
