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

    public BattleState applyMoveForSearch(Move chosenMove) {
        return applyMove(chosenMove).newState();
    }

    public int getTurnNumber()    { return this.turnNumber; }
    public boolean isTrainersTurn() { return this.isTrainersTurn; }

    public MoveOutcome applyMove(Move chosenMove) {
        Pokemon attacker = isTrainersTurn ? trainerPokemon : opponentPokemon;
        Pokemon defender = isTrainersTurn ? opponentPokemon : trainerPokemon;
        double statusRoll = Math.random() * 100;

        DamageResult result = DamageCalc.calculateDamage(chosenMove, attacker, defender);
        int damage = result.damage();

        Pokemon updatedDefender = defender.takeDamage(damage);
        if (statusRoll < chosenMove.statusChance() && chosenMove.statusChance() > 0) {
            updatedDefender = updatedDefender.applyStatusCondition(chosenMove.inflicts());
        }

        if (isTrainersTurn) {
            return new MoveOutcome(new BattleState(trainerPokemon, updatedDefender, turnNumber + 1, false), result);
        } else {
            return new MoveOutcome(new BattleState(updatedDefender, opponentPokemon, turnNumber + 1, true), result);
        }
    }
}