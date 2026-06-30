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

    public BattleState applyMove(Move chosenMove) {
        //if isTrainersTurn {
            //apply move to defender
            //damage = DamageCalc.calculateDamage(chosenMove);
            //Pokemon updatedPokemon = opponentPokemon.takeDamage(damage).applyStatusCondition(chosenMove.inflicts);
            //return new battle state with updated new objects
        //} else {
            //apply move to attacker
        //}
        //return new BattleState(trainerPokemon, opponentPokemon, turnNumber, isTrainersTurn);
    }
}
