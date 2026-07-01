package com.pokemonai;

import com.pokemonai.ai.ExpectimaxAI;
import com.pokemonai.ai.MinimaxAI;
import com.pokemonai.ai.MCTSAI;
import com.pokemonai.engine.BattleEngine;
import com.pokemonai.model.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    static void main() {

        Move tackle      = new Move("Tackle",       Type.NORMAL,   40, 100, 0, StatusCondition.NONE,     MoveCategory.PHYSICAL, 0);
        Move ember       = new Move("Ember",        Type.FIRE,     40, 100, 0, StatusCondition.BURN,     MoveCategory.SPECIAL,  50);
        Move waterGun    = new Move("Water Gun",    Type.WATER,    40, 100, 0, StatusCondition.NONE,     MoveCategory.SPECIAL,  0);
        Move bite        = new Move("Bite",         Type.DARK,     60, 100, 0, StatusCondition.NONE,     MoveCategory.PHYSICAL, 0);
        Move thunderWave = new Move("Thunder Wave", Type.ELECTRIC,  0, 100, 0, StatusCondition.PARALYZE, MoveCategory.STATUS,   100);
        Move poisonPowder= new Move("Poison Powder",Type.POISON,    0,  75, 0, StatusCondition.POISON,   MoveCategory.STATUS,   100);

        StatBlock charmanderStats = new StatBlock(100, 60, 50, 65, 50, 50);
        StatBlock squirtleStats   = new StatBlock(100, 48, 65, 43, 50, 64);

        Pokemon charmander = new Pokemon("Charmander", Type.FIRE, null, charmanderStats, List.of(tackle, ember, thunderWave, poisonPowder), 50);
        Pokemon squirtle = new Pokemon("Squirtle", Type.WATER, null, squirtleStats, List.of(waterGun, bite, thunderWave, poisonPowder), 50);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select mode:");
        System.out.println("  1. MinimaxAI vs MinimaxAI");
        System.out.println("  2. MinimaxAI vs ExpectimaxAI");
        System.out.println("  3. ExpectimaxAI vs ExpectimaxAI");
        System.out.println("  4. MCTSAI vs MCTSAI");
        System.out.println("  5. MinimaxAI vs MCTSAI");
        System.out.println("  6. ExpectimaxAI vs MCTSAI");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.close();

        BattleState state  = new BattleState(charmander, squirtle, 0, true);
        BattleEngine engine = new BattleEngine();

        switch (choice) {

            case 1 -> {
                System.out.println("\nMinimaxAI vs MinimaxAI");
                engine.runBattleVerbose(state, new MinimaxAI(), new MinimaxAI());
            }

            case 2 -> {
                System.out.println("\nMinimaxAI vs ExpectimaxAI");
                engine.runBattleVerbose(state, new MinimaxAI(), new ExpectimaxAI());
            }

            case 3 -> {
                System.out.println("\nExpectimaxAI vs ExpectimaxAI");
                engine.runBattleVerbose(state, new ExpectimaxAI(), new ExpectimaxAI());
            }

            case 4 -> {
                System.out.println("\nMCTSAI vs MCTSAI");
                engine.runBattleVerbose(state, new MCTSAI(), new MCTSAI());
            }

            case 5 -> {
                System.out.println("\nMinimaxAI vs MCTSAI");
                engine.runBattleVerbose(state, new MinimaxAI(), new MCTSAI());
            }

            case 6 -> {
                System.out.println("\nExpectimaxAI vs MCTSAI");
                engine.runBattleVerbose(state, new ExpectimaxAI(), new MCTSAI());
            }

            default -> System.out.println("Invalid choice.");
        }
    }
}