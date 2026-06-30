package com.pokemonai.engine;

import com.pokemonai.ai.AIStrategy;
import com.pokemonai.model.BattleState;
import com.pokemonai.model.Move;
import com.pokemonai.model.Pokemon;

public class BattleEngine {

    public void runBattle(BattleState state, AIStrategy side1, AIStrategy side2) {
        System.out.println("Battle Start");
        System.out.println(state.getTrainerPokemon().getName() + " vs " + state.getOpponentPokemon().getName());
        System.out.println();

        while (!pokemonFainted(state.getTrainerPokemon(), state.getOpponentPokemon())) {
            Move side1Move = side1.makeDecision(state, state.getTrainerPokemon().getMoveList());
            Move side2Move = side2.makeDecision(state, state.getOpponentPokemon().getMoveList());

            Move first;
            Move second;
            boolean trainerGoesFirst;

            if (side1Move.priority() != side2Move.priority()) {
                trainerGoesFirst = side1Move.priority() > side2Move.priority();
            } else {
                trainerGoesFirst = state.getTrainerPokemon().getBaseStats().speed() > state.getOpponentPokemon().getBaseStats().speed();
            }

            first  = trainerGoesFirst ? side1Move : side2Move;
            second = trainerGoesFirst ? side2Move : side1Move;

            // Apply first move
            System.out.println((trainerGoesFirst ? state.getTrainerPokemon().getName() : state.getOpponentPokemon().getName()) + " used " + first.name() + "!");
            state = state.applyMove(first);
            System.out.println("  Trainer HP: " + state.getTrainerPokemon().getCurrentHP() + " | Opponent HP: " + state.getOpponentPokemon().getCurrentHP());

            if (pokemonFainted(state.getTrainerPokemon(), state.getOpponentPokemon())) break;

            // Apply second move
            System.out.println((!trainerGoesFirst ? state.getTrainerPokemon().getName() : state.getOpponentPokemon().getName()) + " used " + second.name() + "!");
            state = state.applyMove(second);
            System.out.println("  Trainer HP: " + state.getTrainerPokemon().getCurrentHP() + " | Opponent HP: " + state.getOpponentPokemon().getCurrentHP());

            if (pokemonFainted(state.getTrainerPokemon(), state.getOpponentPokemon())) break;

            System.out.println();
        }

        // Print winner
        System.out.println("///////// Battle Over //////////");
        if (state.getTrainerPokemon().isFainted()) {
            System.out.println(state.getOpponentPokemon().getName() + " wins");
        } else {
            System.out.println(state.getTrainerPokemon().getName() + " wins");
        }
    }

    private boolean pokemonFainted(Pokemon trainer, Pokemon opponent) {
        return trainer.isFainted() || opponent.isFainted();
    }
}