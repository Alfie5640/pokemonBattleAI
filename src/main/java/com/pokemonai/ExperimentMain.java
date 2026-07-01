package com.pokemonai;

import com.pokemonai.ai.AIStrategy;
import com.pokemonai.ai.ExpectimaxAI;
import com.pokemonai.ai.MCTSAI;
import com.pokemonai.ai.MinimaxAI;
import com.pokemonai.experiments.BattleExperimentRunner;
import com.pokemonai.experiments.ExperimentResult;
import com.pokemonai.model.*;

import java.util.List;
import java.util.function.Supplier;

public class ExperimentMain {

    static void main() {

        Move tackle       = new Move("Tackle",        Type.NORMAL,   40, 100, 0, StatusCondition.NONE,     MoveCategory.PHYSICAL, 0);
        Move ember        = new Move("Ember",         Type.FIRE,     40, 100, 0, StatusCondition.BURN,     MoveCategory.SPECIAL,  50);
        Move waterGun     = new Move("Water Gun",     Type.WATER,    40, 100, 0, StatusCondition.NONE,     MoveCategory.SPECIAL,  0);
        Move bite         = new Move("Bite",          Type.DARK,     60, 100, 0, StatusCondition.NONE,     MoveCategory.PHYSICAL, 0);
        Move thunderWave  = new Move("Thunder Wave",  Type.ELECTRIC,  0, 100, 0, StatusCondition.PARALYZE, MoveCategory.STATUS,   100);
        Move poisonPowder = new Move("Poison Powder", Type.POISON,    0,  75, 0, StatusCondition.POISON,   MoveCategory.STATUS,   100);

        StatBlock charmanderStats = new StatBlock(100, 60, 50, 65, 50, 50);
        StatBlock squirtleStats   = new StatBlock(100, 48, 65, 43, 50, 64);

        // Factories: MUST build a fresh Pokemon each call (full HP, no status).
        // Never close over a single pre-built instance here — Pokemon is
        // immutable, but reusing one instance across games will silently
        // carry over HP/status from the previous battle.
        Supplier<Pokemon> charmanderFactory = () -> new Pokemon(
                "Charmander", Type.FIRE, null, charmanderStats,
                List.of(tackle, ember, thunderWave, poisonPowder), 50
        );

        Supplier<Pokemon> squirtleFactory = () -> new Pokemon(
                "Squirtle", Type.WATER, null, squirtleStats,
                List.of(waterGun, bite, thunderWave, poisonPowder), 50
        );

        BattleExperimentRunner runner = new BattleExperimentRunner();

        // fresh instance per game for AI strategies
        int games = 4000;

        runMatchup(runner, "Minimax", MinimaxAI::new, "Expectimax", ExpectimaxAI::new,
                charmanderFactory, squirtleFactory, games);

        runMatchup(runner, "Minimax", MinimaxAI::new, "MCTS", MCTSAI::new,
                charmanderFactory, squirtleFactory, games);

        runMatchup(runner, "Expectimax", ExpectimaxAI::new, "MCTS", MCTSAI::new,
                charmanderFactory, squirtleFactory, games);
    }

    private static void runMatchup(
            BattleExperimentRunner runner,
            String nameA, Supplier<AIStrategy> aiAFactory,
            String nameB, Supplier<AIStrategy> aiBFactory,
            Supplier<Pokemon> pokemon1Factory,
            Supplier<Pokemon> pokemon2Factory,
            int games
    ) {
        ExperimentResult result = runner.runExperiment(
                aiAFactory.get(), nameA,
                aiBFactory.get(), nameB,
                pokemon1Factory, pokemon2Factory,
                games
        );
        result.printSummary();
        System.out.println();
    }
}