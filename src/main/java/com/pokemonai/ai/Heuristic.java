package com.pokemonai.ai;

import com.pokemonai.model.*;

import java.util.List;

public class Heuristic {

    private static final int WIN_SCORE = 10000;

    public static int evaluateState(BattleState state) {

        Pokemon trainer = state.getTrainerPokemon();
        Pokemon opponent = state.getOpponentPokemon();

        if (trainer.isFainted()) return -WIN_SCORE;
        if (opponent.isFainted()) return WIN_SCORE;

        int score = 0;

        double trainerHP = (double) trainer.getCurrentHP() / trainer.getBaseStats().hp();
        double opponentHP = (double) opponent.getCurrentHP() / opponent.getBaseStats().hp();

        score += (int) ((trainerHP - opponentHP) * 200);

        double trainerEff = getMoveEffScore(trainer.getMoveList(), opponent);
        double opponentEff = getMoveEffScore(opponent.getMoveList(), trainer);

        score += (int) ((trainerEff - opponentEff) * 80);
        score += getStatusScore(trainer, opponent);

        int speedDiff = trainer.getBaseStats().speed() - opponent.getBaseStats().speed();
        score += speedDiff * 2;

        return score;
    }

    private static int getStatusScore(Pokemon trainer, Pokemon opponent) {
        int score = 0;
        score += statusValue(trainer.getStatus(), true);
        score -= statusValue(opponent.getStatus(), false);
        return score;
    }

    private static int statusValue(StatusCondition status, boolean isSelf) {
        int value = switch (status) {
            case NONE -> 0;
            case BURN, POISON, CONFUSION -> 25;
            case PARALYZE -> 28;
            case SLEEP, FREEZE -> 32;
        };
        return isSelf ? -value : value;
    }

    private static double getMoveEffScore(List<Move> moveList, Pokemon opponent) {
        double best = 0.0;
        boolean hasDamagingMove = false;

        for (Move move : moveList) {
            if (move.category() == MoveCategory.STATUS) continue;

            hasDamagingMove = true;
            double eff = move.type().calculateEffectiveness(opponent.getType1());
            if (opponent.getType2() != null) {
                eff *= move.type().calculateEffectiveness(opponent.getType2());
            }

            best = Math.max(best, eff);
        }

        return hasDamagingMove ? best : 1.0;
    }
}