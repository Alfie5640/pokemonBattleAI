package com.pokemonai.model;

import java.util.List;
import java.util.Map;

// Class representing a Pokemon in battle: holds StatBlock, current HP,
// stat stages, type, moveset. Handles damage calculation and status
public class Pokemon {
    String name;
    Type type1;
    Type type2;
    StatBlock baseStats;
    int currentHP;
    List<Move> moveList;
    int level;
    StatusCondition status;
    Map<Stat, Integer> statStages;

    public Pokemon(String name, Type type1, Type type2, StatBlock baseStats, List<Move> moveList, int level, Map<Stat, Integer> statStages) {
        this(name, type1, type2, baseStats, baseStats.hp(), moveList, level, statStages, StatusCondition.NONE);
    }

    public Pokemon(String name, Type type1, Type type2, StatBlock baseStats, int currentHP, List<Move> moveList, int level, Map<Stat, Integer> statStages, StatusCondition status) {
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.baseStats = baseStats;
        this.currentHP = currentHP;
        this.moveList = moveList;
        this.level = level;
        this.statStages = statStages;
        this.status = status;
    }

    public List<Move> getMoveList() { return this.moveList; }
    public int getCurrentHP() {
        return this.currentHP;
    }

    public StatBlock getBaseStats() {
        return this.baseStats;
    }

    //Return new Pokemon to maintain immutability for AI strategies
    public Pokemon takeDamage(int amount) {
        return new Pokemon(name, type1, type2, baseStats, (currentHP-amount), moveList, level, statStages, status);
    }

    public Type getType1() {
        return this.type1;
    }

    public Type getType2() {
        return this.type2;
    }

    public StatusCondition getStatus() {return this.status; }

    public Pokemon applyStatusCondition(StatusCondition newStatus) {
        return new Pokemon(name, type1, type2, baseStats, currentHP, moveList, level, statStages, newStatus);
    }

    public boolean isFainted() {
        return currentHP <= 0;
    }


    public String getName() {
        return this.name;
    }
}
