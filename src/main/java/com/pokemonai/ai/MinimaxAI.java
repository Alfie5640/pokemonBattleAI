package com.pokemonai.ai;

import com.pokemonai.model.BattleState;
import com.pokemonai.model.Move;

import java.util.List;

public class MinimaxAI implements AIStrategy {

    @Override
    public Move makeDecision(BattleState state, List<Move> moveList) {

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        int depth = 3;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (Move move : moveList) {

            BattleState nextState = state.applyMoveForSearch(move);
            int score = minimax(nextState, depth - 1, false, alpha, beta);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = bestScore;
            }
        }

        return bestMove;
    }

    private int minimax(BattleState state, int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth == 0 || state.getTrainerPokemon().isFainted() || state.getOpponentPokemon().isFainted()) {
            return Heuristic.evaluateState(state);
        }

        List<Move> moveList;

        if (isMaximizing) {

            int best = Integer.MIN_VALUE;
            moveList = state.getTrainerPokemon().getMoveList();

            for (Move move : moveList) {
                BattleState nextState = state.applyMoveForSearch(move);
                int score = minimax(nextState, depth - 1, false, alpha, beta);

                best = Math.max(best, score);
                alpha = Math.max(alpha, best);

                if (beta <= alpha) break;
            }

            return best;

        } else {

            int best = Integer.MAX_VALUE;
            moveList = state.getOpponentPokemon().getMoveList();

            for (Move move : moveList) {
                BattleState nextState = state.applyMoveForSearch(move);
                int score = minimax(nextState, depth - 1, true, alpha, beta);

                best = Math.min(best, score);
                beta = Math.min(beta, best);

                if (beta <= alpha) break;
            }

            return best;
        }
    }
}