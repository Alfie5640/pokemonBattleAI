package com.pokemonai.model;

public class DamageCalc {
    public static double calculateDamage(Move move, Pokemon attacker, Pokemon defender) {
        int atkStat;
        int defStat;
        double baseDmg;
        double STAB = 1;
        double roll = ((Math.random() * (1.0 - 0.85)) + 0.85);

        if(move.category() == MoveCategory.PHYSICAL) {
            atkStat = attacker.baseStats.atk();
            defStat = defender.baseStats.def();
        } else if(move.category() == MoveCategory.SPECIAL) {
            atkStat = attacker.baseStats.spAtk();
            defStat = defender.baseStats.spDef();
        } else {
            return 0;
        }

        baseDmg = ((2 * (double)attacker.level / 5 + 2) * move.basePower() * (double)atkStat / (double)defStat) / 50 + 2;
        double effectiveness = move.type().calculateEffectiveness(defender.type1);
        if (defender.type2 != null) {
            effectiveness *= move.type().calculateEffectiveness(defender.type2);
        }

        if ((attacker.type1 == move.type()|| attacker.type2 == move.type())) {
            STAB = 1.5;
        }


        return baseDmg * effectiveness * STAB * roll;
    }
}
