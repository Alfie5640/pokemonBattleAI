package com.pokemonai.model;

public record MoveOutcome(BattleState newState, DamageResult damageResult) {
}
