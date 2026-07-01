package com.pokemonai.engine;

import com.pokemonai.ai.AIStrategy;
import com.pokemonai.model.*;

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
                trainerGoesFirst = state.getTrainerPokemon().getBaseStats().speed()
                        > state.getOpponentPokemon().getBaseStats().speed();
            }

            first  = trainerGoesFirst ? side1Move : side2Move;
            second = trainerGoesFirst ? side2Move : side1Move;

            // Try wake/thaw before first move
            state = tryStatusRecovery(state, trainerGoesFirst);

            // Apply first move
            Pokemon firstAttacker = trainerGoesFirst
                    ? state.getTrainerPokemon()
                    : state.getOpponentPokemon();

            System.out.println(firstAttacker.getName() + " used " + first.name() + "!");
            if (canMove(firstAttacker)) {
                Pokemon defenderBefore = trainerGoesFirst
                        ? state.getOpponentPokemon()
                        : state.getTrainerPokemon();

                MoveOutcome outcome = state.applyMove(first);
                state = outcome.newState();
                DamageResult result = outcome.damageResult();

                printMoveEffects(first, defenderBefore, state, result, trainerGoesFirst);
            } else {
                System.out.println("  " + firstAttacker.getName() + " is "
                        + firstAttacker.getStatus().toString().toLowerCase() + " and can't move!");
            }
            System.out.println("  Trainer HP: " + state.getTrainerPokemon().getCurrentHP()
                    + " | Opponent HP: " + state.getOpponentPokemon().getCurrentHP());

            if (pokemonFainted(state.getTrainerPokemon(), state.getOpponentPokemon())) break;

            // Try wake/thaw before second move
            state = tryStatusRecovery(state, !trainerGoesFirst);

            // Apply second move
            Pokemon secondAttacker = trainerGoesFirst
                    ? state.getOpponentPokemon()
                    : state.getTrainerPokemon();

            System.out.println(secondAttacker.getName() + " used " + second.name() + "!");
            if (canMove(secondAttacker)) {
                Pokemon defenderBefore = trainerGoesFirst
                        ? state.getTrainerPokemon()
                        : state.getOpponentPokemon();

                MoveOutcome outcome = state.applyMove(second);
                state = outcome.newState();
                DamageResult result = outcome.damageResult();

                printMoveEffects(second, defenderBefore, state, result, !trainerGoesFirst);
            } else {
                System.out.println("  " + secondAttacker.getName() + " is "
                        + secondAttacker.getStatus().toString().toLowerCase() + " and can't move!");
            }
            System.out.println("  Trainer HP: " + state.getTrainerPokemon().getCurrentHP()
                    + " | Opponent HP: " + state.getOpponentPokemon().getCurrentHP());

            if (pokemonFainted(state.getTrainerPokemon(), state.getOpponentPokemon())) break;

            // End of turn effects
            state = applyEndOfTurn(state);
            System.out.println("  [EOT] Trainer HP: " + state.getTrainerPokemon().getCurrentHP()
                    + " | Opponent HP: " + state.getOpponentPokemon().getCurrentHP());

            if (pokemonFainted(state.getTrainerPokemon(), state.getOpponentPokemon())) break;

            System.out.println();
        }

        System.out.println("///////// Battle Over //////////");
        if (state.getTrainerPokemon().isFainted()) {
            System.out.println(state.getOpponentPokemon().getName() + " wins");
        } else {
            System.out.println(state.getTrainerPokemon().getName() + " wins");
        }
    }

    private void printMoveEffects(Move move, Pokemon defenderBefore,
                                  BattleState stateAfter, DamageResult result,
                                  boolean attackerIsTrainer) {
        // Miss
        if (result.damage() == 0 && move.basePower() > 0) {
            System.out.println("  " + move.name() + " missed!");
            return;
        }

        // Crit
        if (result.wasCrit()) {
            System.out.println("  A critical hit!");
        }

        // Type effectiveness
        double eff = result.effectiveness();
        if (eff == 0.0) {
            System.out.println("  It doesn't affect " + defenderBefore.getName() + "...");
        } else if (eff > 1.0) {
            System.out.println("  It's super effective!");
        } else if (eff < 1.0) {
            System.out.println("  It's not very effective...");
        }

        // Status infliction
        Pokemon defenderAfter = attackerIsTrainer
                ? stateAfter.getOpponentPokemon()
                : stateAfter.getTrainerPokemon();
        printStatusIfInflicted(defenderBefore, defenderAfter);
    }

    private void printStatusIfInflicted(Pokemon defenderBefore, Pokemon defenderAfter) {
        if (defenderAfter.getStatus() != StatusCondition.NONE
                && defenderAfter.getStatus() != defenderBefore.getStatus()) {
            System.out.println("  " + defenderAfter.getName()
                    + " was inflicted with " + defenderAfter.getStatus() + "!");
        }
    }

    private BattleState tryStatusRecovery(BattleState state, boolean isTrainerSide) {
        Pokemon pokemon = isTrainerSide
                ? state.getTrainerPokemon()
                : state.getOpponentPokemon();

        Pokemon recovered = tryWakeOrThaw(pokemon);

        if (isTrainerSide) {
            return new BattleState(recovered, state.getOpponentPokemon(),
                    state.getTurnNumber(), state.isTrainersTurn());
        } else {
            return new BattleState(state.getTrainerPokemon(), recovered,
                    state.getTurnNumber(), state.isTrainersTurn());
        }
    }

    private Pokemon tryWakeOrThaw(Pokemon pokemon) {
        return switch (pokemon.getStatus()) {
            case SLEEP -> {
                if (Math.random() < 0.33) {
                    System.out.println("  " + pokemon.getName() + " woke up!");
                    yield pokemon.applyStatusCondition(StatusCondition.NONE);
                }
                yield pokemon;
            }
            case FREEZE -> {
                if (Math.random() < 0.20) {
                    System.out.println("  " + pokemon.getName() + " thawed out!");
                    yield pokemon.applyStatusCondition(StatusCondition.NONE);
                }
                yield pokemon;
            }
            default -> pokemon;
        };
    }

    private boolean canMove(Pokemon pokemon) {
        return switch (pokemon.getStatus()) {
            case SLEEP, FREEZE -> false;
            case PARALYZE      -> Math.random() >= 0.25;
            case CONFUSION     -> Math.random() >= 0.50;
            default            -> true;
        };
    }

    private BattleState applyEndOfTurn(BattleState state) {
        Pokemon updatedTrainer  = applyEOTEffects(state.getTrainerPokemon());
        Pokemon updatedOpponent = applyEOTEffects(state.getOpponentPokemon());
        return new BattleState(updatedTrainer, updatedOpponent,
                state.getTurnNumber(), state.isTrainersTurn());
    }

    private Pokemon applyEOTEffects(Pokemon pokemon) {
        int chip = switch (pokemon.getStatus()) {
            case BURN   -> (int) Math.round(pokemon.getBaseStats().hp() / 16.0);
            case POISON -> (int) Math.round(pokemon.getBaseStats().hp() / 8.0);
            default     -> 0;
        };
        if (chip > 0) {
            System.out.println("  " + pokemon.getName() + " is hurt by "
                    + pokemon.getStatus().toString().toLowerCase() + "! (-" + chip + " HP)");
        }
        return pokemon.takeDamage(chip);
    }

    private boolean pokemonFainted(Pokemon trainer, Pokemon opponent) {
        return trainer.isFainted() || opponent.isFainted();
    }
}