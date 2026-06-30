package com.pokemonai;

import com.pokemonai.ai.MinimaxAI;
import com.pokemonai.engine.BattleEngine;
import com.pokemonai.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        Move tackle   = new Move("Tackle",    Type.NORMAL, 40, 100, 0, StatusCondition.NONE, MoveCategory.PHYSICAL);
        Move ember    = new Move("Ember",     Type.FIRE,   40, 100, 0, StatusCondition.NONE, MoveCategory.SPECIAL);
        Move waterGun = new Move("Water Gun", Type.WATER,  40, 100, 0, StatusCondition.NONE, MoveCategory.SPECIAL);
        Move bite     = new Move("Bite",      Type.DARK,   60, 100, 0, StatusCondition.NONE, MoveCategory.PHYSICAL);

        StatBlock charmanderStats = new StatBlock(100, 60, 50, 65, 50, 50);
        StatBlock squirtleStats   = new StatBlock(100, 48, 65, 43, 50, 64);

        Map<Stat, Integer> emptyStages = new HashMap<>();

        Pokemon charmander = new Pokemon(
                "Charmander", Type.FIRE, null, charmanderStats,
                List.of(tackle, ember), 50, emptyStages
        );

        Pokemon squirtle = new Pokemon(
                "Squirtle", Type.WATER, null, squirtleStats,
                List.of(waterGun, bite), 50, emptyStages
        );

        BattleState state = new BattleState(charmander, squirtle, 0, true);

        //AI vs AI
        BattleEngine engine = new BattleEngine();
        engine.runBattle(state, new MinimaxAI(), new MinimaxAI());
    }
}