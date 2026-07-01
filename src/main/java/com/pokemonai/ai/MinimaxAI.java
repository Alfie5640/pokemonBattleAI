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
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        boolean isMaximizing = true;

        for (Move move : moveList) {
            BattleState nextState = state.applyMoveForSearch(move);
            int score = minimax(nextState, depth, !(isMaximizing), alpha, beta);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = score;
            }
        }
        return bestMove;
    }

    private int minimax(BattleState state, int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth == 0 || state.getTrainerPokemon().isFainted() || state.getOpponentPokemon().isFainted()) {
            return Heuristic.evaluateState(state);
        }

        int best;
        List<Move> moveList;
        if (isMaximizing) {
            best = Integer.MIN_VALUE;
            moveList = state.getTrainerPokemon().getMoveList();
            for (Move move : moveList) {
                int score = minimax(state.applyMoveForSearch(move), depth - 1, false, alpha, beta);
                best = Math.max(best, score);
                alpha = Math.max(alpha, best);
                if (beta <= alpha) { break; }
            }
        } else {
            best = Integer.MAX_VALUE;
            moveList = state.getOpponentPokemon().getMoveList();
            for (Move move : moveList) {
                int score = minimax(state.applyMoveForSearch(move), depth - 1, true, alpha, beta);
                best = Math.min(best, score);
                beta = Math.min(beta, best);
                if (beta <= alpha) { break; }
            }
        }
        return best;
    }

}
