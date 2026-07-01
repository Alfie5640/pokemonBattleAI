package com.pokemonai.engine;

import com.pokemonai.ai.AIStrategy;
import com.pokemonai.model.*;

import java.util.PriorityQueue;

public class BattleEngine {

    private static final int MAX_TURNS = 100;

    public void runBattleVerbose(BattleState state, AIStrategy side1, AIStrategy side2) {
        boolean verbose = true;
        runBattle(state, side1, side2, verbose);
    }

    public BattleResult runBattleSilent(BattleState state, AIStrategy side1, AIStrategy side2) {
        boolean verbose = false;
        return runBattle(state, side1, side2, verbose);
    }

    private BattleResult runBattle(BattleState state, AIStrategy side1, AIStrategy side2, boolean verbose) {
        if (verbose) {
            System.out.println("Battle Start");
            System.out.println(state.getTrainerPokemon().getName() + " vs " + state.getOpponentPokemon().getName());
            System.out.println();
        }

        boolean hitTurnCap = false;

        while (!pokemonFainted(state.getTrainerPokemon(), state.getOpponentPokemon())) {

            if (state.getTurnNumber() >= MAX_TURNS) {
                hitTurnCap = true;
                if (verbose) {
                    System.out.println("  [Turn cap reached (" + MAX_TURNS + ") - ending battle as a draw]");
                }
                break;
            }

            BattleState turnState = state;
            BattleState workingState = state;

            Move side1Move = side1.makeDecision(turnState, turnState.getTrainerPokemon().getMoveList());
            Move side2Move = side2.makeDecision(turnState, turnState.getOpponentPokemon().getMoveList());

            PriorityQueue<BattleAction> queue = new PriorityQueue<>((a, b) -> {

                int priorityCompare = Integer.compare(b.move().priority(), a.move().priority());
                if (priorityCompare != 0) return priorityCompare;
                return Integer.compare(b.pokemon().getBaseStats().speed(), a.pokemon().getBaseStats().speed());

            });

            queue.add(new BattleAction(state.getTrainerPokemon(), side1Move, true));
            queue.add(new BattleAction(state.getOpponentPokemon(), side2Move, false));

            while (!queue.isEmpty()) {

                BattleAction action = queue.poll();
                workingState = tryStatusRecovery(workingState, action.trainerSide(), verbose);

                Pokemon attacker = action.trainerSide() ? workingState.getTrainerPokemon() : workingState.getOpponentPokemon();
                if (verbose) { System.out.println(attacker.getName() + " used " + action.move().name() + "!"); }

                if (canMove(attacker)) {

                    Pokemon defenderBefore = action.trainerSide() ? workingState.getOpponentPokemon() : workingState.getTrainerPokemon();

                    MoveOutcome outcome = workingState.applyMove(action.move());
                    workingState = outcome.newState();
                    DamageResult result = outcome.damageResult();

                    if (verbose) { printMoveEffects(action.move(), defenderBefore, workingState, result, action.trainerSide()); }

                } else {
                    if (verbose) { System.out.println("  " + attacker.getName() + " is " + attacker.getStatus().toString().toLowerCase() + " and can't move!"); }
                }

                if (verbose) { System.out.println("  Trainer HP: " + workingState.getTrainerPokemon().getCurrentHP() + " | Opponent HP: " + workingState.getOpponentPokemon().getCurrentHP()); }

                if (pokemonFainted(workingState.getTrainerPokemon(), workingState.getOpponentPokemon())) {
                    break;
                }
            }

            workingState = applyEndOfTurn(workingState, verbose);
            if (verbose) { System.out.println("  [EOT] Trainer HP: " + workingState.getTrainerPokemon().getCurrentHP() + " | Opponent HP: " + workingState.getOpponentPokemon().getCurrentHP()); }
            state = workingState;

            if (pokemonFainted(state.getTrainerPokemon(), state.getOpponentPokemon())) {
                break;
            }

            if (verbose) { System.out.println(); }
        }

        if (verbose) { System.out.println("///////// Battle Over //////////"); }

        if (hitTurnCap) {
            if (verbose) { System.out.println("Draw (turn cap reached)"); }
            return new BattleResult(BattleResult.Outcome.DRAW, state.getTurnNumber());
        }

        boolean trainerWon = !state.getTrainerPokemon().isFainted();

        if (verbose) {
            String winnerName = trainerWon ? state.getTrainerPokemon().getName() : state.getOpponentPokemon().getName();
            System.out.println(winnerName + " wins");
        }

        return new BattleResult(trainerWon ? BattleResult.Outcome.TRAINER_WON : BattleResult.Outcome.OPPONENT_WON, state.getTurnNumber());
    }

    private void printMoveEffects(Move move, Pokemon defenderBefore, BattleState stateAfter, DamageResult result, boolean attackerIsTrainer) {
        if (result.damage() == 0 && move.basePower() > 0) {
            System.out.println("  " + move.name() + " missed!");
            return;
        }

        if (result.wasCrit()) {
            System.out.println("  A critical hit!");
        }

        double eff = result.effectiveness();

        if (eff == 0.0) {
            System.out.println("  It doesn't affect " + defenderBefore.getName() + "...");
        } else if (eff > 1.0) {
            System.out.println("  It's super effective!");
        } else if (eff < 1.0) {
            System.out.println("  It's not very effective...");
        }

        Pokemon defenderAfter = attackerIsTrainer ? stateAfter.getOpponentPokemon() : stateAfter.getTrainerPokemon();
        printStatusIfInflicted(defenderBefore, defenderAfter);
    }

    private void printStatusIfInflicted(Pokemon before, Pokemon after) {
        if (after.getStatus() != StatusCondition.NONE && after.getStatus() != before.getStatus()) {
            System.out.println("  " + after.getName() + " was inflicted with " + after.getStatus() + "!");
        }
    }

    private BattleState tryStatusRecovery(BattleState state, boolean isTrainerSide, boolean verbose) {

        Pokemon pokemon = isTrainerSide ? state.getTrainerPokemon() : state.getOpponentPokemon();
        Pokemon recovered = tryWakeOrThaw(pokemon, verbose);
        return isTrainerSide ? new BattleState(recovered, state.getOpponentPokemon(), state.getTurnNumber(), state.isTrainersTurn()) : new BattleState(state.getTrainerPokemon(), recovered, state.getTurnNumber(), state.isTrainersTurn());
    }

    private Pokemon tryWakeOrThaw(Pokemon pokemon, boolean verbose) {
        return switch (pokemon.getStatus()) {

            case SLEEP -> {
                if (Math.random() < 0.33) {
                    if (verbose) { System.out.println("  " + pokemon.getName() + " woke up!"); }
                    yield pokemon.applyStatusCondition(StatusCondition.NONE);
                }
                yield pokemon;
            }

            case FREEZE -> {
                if (Math.random() < 0.20) {
                    if (verbose) { System.out.println("  " + pokemon.getName() + " thawed out!"); }
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
            case PARALYZE -> Math.random() >= 0.25;
            case CONFUSION -> Math.random() >= 0.50;
            default -> true;
        };
    }

    public BattleState applyEndOfTurn(BattleState state, boolean verbose) {

        Pokemon trainer = applyEOTEffects(state.getTrainerPokemon(), verbose);
        Pokemon opponent = applyEOTEffects(state.getOpponentPokemon(), verbose);

        return new BattleState(trainer, opponent, state.getTurnNumber(), state.isTrainersTurn());
    }

    private Pokemon applyEOTEffects(Pokemon pokemon, boolean verbose) {

        int chip = switch (pokemon.getStatus()) {

            case BURN -> (int) Math.round(pokemon.getBaseStats().hp() / 16.0);
            case POISON -> (int) Math.round(pokemon.getBaseStats().hp() / 8.0);
            default -> 0;
        };

        if (chip > 0 && verbose) {
            System.out.println("  " + pokemon.getName() + " is hurt by " + pokemon.getStatus().toString().toLowerCase() + "! (-" + chip + " HP)");
        }

        return pokemon.takeDamage(chip);
    }

    private boolean pokemonFainted(Pokemon trainer, Pokemon opponent) {
        return trainer.isFainted() || opponent.isFainted();
    }
}