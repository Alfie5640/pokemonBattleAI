package com.pokemonai.model;

public class DamageCalc {

    public static DamageResult calculateDamage(Move move, Pokemon attacker, Pokemon defender) {
        int atkStat;
        int defStat;

        double roll = (Math.random() * (1.0 - 0.85)) + 0.85;
        double hitChance = Math.random() * 100;
        double critChance = Math.random();

        if (move.accuracy() < hitChance) {
            return new DamageResult(0, 1.0, false);
        }

        boolean crit = critChance < 0.0625;

        if (move.category() == MoveCategory.PHYSICAL) {
            atkStat = attacker.baseStats.atk();

            if (attacker.getStatus() == StatusCondition.BURN) {
                atkStat = (int) (atkStat * 0.5);
            }

            defStat = defender.baseStats.def();

        } else if (move.category() == MoveCategory.SPECIAL) {
            atkStat = attacker.baseStats.spAtk();
            defStat = defender.baseStats.spDef();
        } else {
            return new DamageResult(0, 1.0, false);
        }

        double baseDmg = ((2 * (double) attacker.level / 5 + 2) * move.basePower() * (double) atkStat / defStat) / 50 + 2;
        double effectiveness = move.type().calculateEffectiveness(defender.type1);

        if (defender.type2 != null) {
            effectiveness *= move.type().calculateEffectiveness(defender.type2);
        }

        double STAB = (attacker.type1 == move.type() || attacker.type2 == move.type()) ? 1.5 : 1.0;
        double totalDmg = baseDmg * effectiveness * STAB * roll;

        if (crit) {
            totalDmg *= 1.5;
        }

        return new DamageResult((int) Math.round(totalDmg), effectiveness, crit

        );
    }
}