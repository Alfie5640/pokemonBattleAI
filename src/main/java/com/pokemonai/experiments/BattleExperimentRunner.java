package com.pokemonai.experiments;

import com.pokemonai.ai.AIStrategy;
import com.pokemonai.engine.BattleEngine;
import com.pokemonai.engine.BattleResult;
import com.pokemonai.model.BattleState;
import com.pokemonai.model.Pokemon;

import java.util.function.Supplier;

public class BattleExperimentRunner {

    // KNOWN LIMITATION - read before trusting aWinRateAsTrainer/aWinRateAsOpponent:
    //
    // BattleState.applyMove (used by every AIStrategy's lookahead search)
    // models turns as strictly alternating single moves. attacker acts,
    // then it becomes the defender's turn. BattleEngine.runBattle instead
    // has both sides commit a move simultaneously each turn, then resolves
    // actual order via priority + speed
    //
    // This means every AI's search always reasons "I act, then the
    // opponent responds" from its own root, regardless of which side is
    // faster in a given matchup. This produces a
    // persistent, reproducible trainer-slot advantage (confirmed to persist
    // at 4000 games, so it's not sampling noise) that is not a fairness
    // bug in this runner. It shows a real mismatch between how the
    // AIs search and how the engine resolves turns
    //
    // aWinRate (the overall win rate, averaged across all four blocks) is
    // not affected by this as each AI plays the trainer slot exactly as
    // often as the opponent slot, so the asymmetry cancels out of the
    // aggregate. Only the per-slot breakdown (aWinRateAsTrainer vs
    // aWinRateAsOpponent) reflects this, and should be read as
    // "known search-model artifact", not "this AI performs better/worse
    // as trainer." Fixing this properly would mean reworking the search
    // to jointly select both sides' moves per ply instead of assuming
    // alternation which is a bigger change deferred for now

    private final BattleEngine engine = new BattleEngine();

    public ExperimentResult runExperiment(
            AIStrategy aiA, String aiAName,
            AIStrategy aiB, String aiBName,
            Supplier<Pokemon> speciesXFactory,
            Supplier<Pokemon> speciesYFactory,
            int totalGames
    ) {
        if (totalGames % 4 != 0) {
            throw new IllegalArgumentException("totalGames must be divisible by 4 to split evenly across AI and species assignment, got " + totalGames);
        }

        int gamesPerBlock = totalGames / 4;

        Tally tally = new Tally();

        // protect against species bias
        runBlock(tally, aiA, true, aiB, speciesXFactory, speciesYFactory, gamesPerBlock);
        runBlock(tally, aiB, false, aiA, speciesXFactory, speciesYFactory, gamesPerBlock);
        runBlock(tally, aiA, true, aiB, speciesYFactory, speciesXFactory, gamesPerBlock);
        runBlock(tally, aiB, false, aiA, speciesYFactory, speciesXFactory, gamesPerBlock);

        double aWinRate = (double) tally.aWins / totalGames;
        double aWinRateAsTrainer = (double) tally.aWinsAsTrainer / (gamesPerBlock * 2);
        double aWinRateAsOpponent = (double) tally.aWinsAsOpponent / (gamesPerBlock * 2);
        double avgTurns = (double) tally.totalTurns / totalGames;

        return new ExperimentResult(aiAName, aiBName, totalGames, tally.aWins, tally.bWins, tally.draws,
                aWinRate, aWinRateAsTrainer, aWinRateAsOpponent, avgTurns);
    }

    private void runBlock(
            Tally tally,
            AIStrategy trainerAI, boolean trainerIsA, AIStrategy opponentAI,
            Supplier<Pokemon> trainerSpeciesFactory, Supplier<Pokemon> opponentSpeciesFactory,
            int games
    ) {
        for (int i = 0; i < games; i++) {
            BattleState state = new BattleState(trainerSpeciesFactory.get(), opponentSpeciesFactory.get(), 0, true);
            BattleResult result = engine.runBattleSilent(state, trainerAI, opponentAI);
            tally.totalTurns += result.turnCount();

            if (result.isDraw()) {
                tally.draws++;
            } else if (result.trainerWon()) {
                if (trainerIsA) {
                    tally.aWins++;
                    tally.aWinsAsTrainer++;
                } else {
                    tally.bWins++;
                }
            } else {
                if (trainerIsA) {
                    tally.bWins++;
                } else {
                    tally.aWins++;
                    tally.aWinsAsOpponent++;
                }
            }
        }
    }

    private static class Tally {
        int aWins = 0, bWins = 0, draws = 0;
        int aWinsAsTrainer = 0, aWinsAsOpponent = 0;
        long totalTurns = 0;
    }
}