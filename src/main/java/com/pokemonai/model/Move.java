package com.pokemonai.model;

// Record storing move data: name, type, base power, accuracy, priority
public record Move(String name, Type type, int basePower, int accuracy, int priority, StatusCondition inflicts, MoveCategory category, int statusChance) {
}
