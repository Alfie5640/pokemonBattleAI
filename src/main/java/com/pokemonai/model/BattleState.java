package com.pokemonai.model;

// Immutable snapshot of the entire battle at one moment
public class BattleState {
    Pokemon trainerPokemon;
    Pokemon opponentPokemon;
    int turnNumber;
    boolean isTrainersTurn;

    public BattleState(Pokemon trainerPokemon, Pokemon opponentPokemon, int turnNumber, boolean isTrainersTurn) {
        this.trainerPokemon = trainerPokemon;
        this.opponentPokemon = opponentPokemon;
        this.turnNumber = turnNumber;
        this.isTrainersTurn = isTrainersTurn;
    }

    public Pokemon getTrainerPokemon() {
        return this.trainerPokemon;
    }

    public Pokemon getOpponentPokemon() {
        return this.opponentPokemon;
    }

    public BattleState applyMove(Move chosenMove) {
        Pokemon attacker = isTrainersTurn ? trainerPokemon : opponentPokemon;
        Pokemon defender = isTrainersTurn ? opponentPokemon : trainerPokemon;

        int damage = (int) Math.round(DamageCalc.calculateDamage(chosenMove, attacker, defender));
        Pokemon updatedDefender = defender.takeDamage(damage).applyStatusCondition(chosenMove.inflicts());

        if (isTrainersTurn) {
            return new BattleState(trainerPokemon, updatedDefender, turnNumber + 1, false);
        } else {
            return new BattleState(updatedDefender, opponentPokemon, turnNumber + 1, true);
        }
    }
}