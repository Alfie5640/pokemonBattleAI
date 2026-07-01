package com.pokemonai.engine;

import com.pokemonai.model.Move;
import com.pokemonai.model.Pokemon;

public record BattleAction(Pokemon pokemon, Move move, boolean trainerSide) {
}
