package com.pokemonai;

import com.pokemonai.ai.ExpectimaxAI;
import com.pokemonai.ai.MinimaxAI;
import com.pokemonai.engine.BattleEngine;
import com.pokemonai.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Move tackle      = new Move("Tackle",       Type.NORMAL,   40, 100, 0, StatusCondition.NONE,     MoveCategory.PHYSICAL, 0);
        Move ember       = new Move("Ember",        Type.FIRE,     40, 100, 0, StatusCondition.BURN,     MoveCategory.SPECIAL,  50);
        Move waterGun    = new Move("Water Gun",    Type.WATER,    40, 100, 0, StatusCondition.NONE,     MoveCategory.SPECIAL,  0);
        Move bite        = new Move("Bite",         Type.DARK,     60, 100, 0, StatusCondition.NONE,     MoveCategory.PHYSICAL, 0);
        Move thunderWave = new Move("Thunder Wave", Type.ELECTRIC,  0, 100, 0, StatusCondition.PARALYZE, MoveCategory.STATUS,   100);
        Move poisonPowder= new Move("Poison Powder",Type.POISON,    0,  75, 0, StatusCondition.POISON,   MoveCategory.STATUS,   100);

        StatBlock charmanderStats = new StatBlock(100, 60, 50, 65, 50, 50);
        StatBlock squirtleStats   = new StatBlock(100, 48, 65, 43, 50, 64);

        Map<Stat, Integer> emptyStages = new HashMap<>();

        Pokemon charmander = new Pokemon(
                "Charmander", Type.FIRE, null, charmanderStats,
                List.of(tackle, ember, thunderWave, poisonPowder), 50, emptyStages
        );

        Pokemon squirtle = new Pokemon(
                "Squirtle", Type.WATER, null, squirtleStats,
                List.of(waterGun, bite, thunderWave, poisonPowder), 50, emptyStages
        );

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Pokemon Battle AI ===");
        System.out.println("Select mode:");
        System.out.println("  1. MinimaxAI vs MinimaxAI");
        System.out.println("  2. MinimaxAI vs ExpectimaxAI");
        System.out.println("  3. ExpectimaxAI vs ExpectimaxAI");
        System.out.print("Enter choice: ");

        int choice = scanner.nextInt();
        scanner.close();

        BattleState state  = new BattleState(charmander, squirtle, 0, true);
        BattleEngine engine = new BattleEngine();

        switch (choice) {
            case 1 -> {
                System.out.println("\nMinimaxAI vs MinimaxAI");
                engine.runBattle(state, new MinimaxAI(), new MinimaxAI());
            }
            case 2 -> {
                System.out.println("\nMinimaxAI vs ExpectimaxAI");
                engine.runBattle(state, new MinimaxAI(), new ExpectimaxAI());
            }
            case 3 -> {
                System.out.println("\nExpectimaxAI vs ExpectimaxAI");
                engine.runBattle(state, new ExpectimaxAI(), new ExpectimaxAI());
            }
            default -> System.out.println("Invalid choice.");
        }
    }
}