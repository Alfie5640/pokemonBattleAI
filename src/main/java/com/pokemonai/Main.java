package com.pokemonai;

import com.pokemonai.ai.MinimaxAI;
import com.pokemonai.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Move tackle = new Move("Tackle", Type.NORMAL, 40, 100, 0, StatusCondition.NONE, MoveCategory.PHYSICAL);
        Move ember  = new Move("Ember", Type.FIRE, 40, 100, 0, StatusCondition.NONE, MoveCategory.SPECIAL);

        StatBlock trainerStats = new StatBlock(100, 60, 50, 70, 50, 50);
        StatBlock opponentStats = new StatBlock(100, 55, 55, 60, 45, 45);

        Map<Stat, Integer> emptyStages = new HashMap<>();

        Pokemon trainerMon = new Pokemon(
                "Charmander", Type.FIRE, null, trainerStats,
                List.of(tackle, ember), 50, emptyStages
        );

        Pokemon opponentMon = new Pokemon(
                "Squirtle", Type.WATER, null, opponentStats,
                List.of(tackle), 50, emptyStages
        );

        BattleState state = new BattleState(trainerMon, opponentMon, 0, true);
        MinimaxAI ai = new MinimaxAI();
        Move chosenMove = ai.makeDecision(state, trainerMon.getMoveList());

        System.out.println("AI chose: " + chosenMove.name());
    }
}