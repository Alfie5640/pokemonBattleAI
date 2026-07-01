package com.pokemonai.ai;

import com.pokemonai.model.BattleState;
import com.pokemonai.model.Move;
import com.pokemonai.model.Pokemon;

import java.util.List;

// Scores a BattleState as a single number.
public class Heuristic {
    public static int evaluateState(BattleState currentState) {
        int score = 0;

        double oppHP = ((double)currentState.getOpponentPokemon().getCurrentHP()) / (currentState.getOpponentPokemon().getBaseStats().hp()) * 100 ;
        double trainerHP = ((double)currentState.getTrainerPokemon().getCurrentHP()) / (currentState.getTrainerPokemon().getBaseStats().hp()) * 100;

        score += (int)((trainerHP - oppHP) * 5);

        double trainerEff = getMoveEffScore(currentState.getTrainerPokemon().getMoveList(), currentState.getOpponentPokemon());
        double oppEff = getMoveEffScore(currentState.getOpponentPokemon().getMoveList(), currentState.getTrainerPokemon());

        score += (int)((trainerEff - oppEff) * 50);

        return score;
    }

    private static double getMoveEffScore(List<Move> moveList, Pokemon opponent) {
        double score = 0;
        for (Move move : moveList) {
            double effectiveness = (move.type().calculateEffectiveness(opponent.getType1()));
            if (opponent.getType2() != null) {
                effectiveness *= (move.type().calculateEffectiveness(opponent.getType2()));
            }

            score = Math.max(score, effectiveness);
        }

        return score;
    }

    private double normalizeValue(double value, double min, double max) {
        return ((value - min) / (max - min));
    }
}
