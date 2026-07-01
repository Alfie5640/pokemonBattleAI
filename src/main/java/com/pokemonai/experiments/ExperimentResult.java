package com.pokemonai.experiments;

public record ExperimentResult(
        String aiAName,
        String aiBName,
        int gamesPlayed,
        int aWins,
        int bWins,
        int draws,
        double aWinRate,
        double aWinRateAsTrainer,   // see BattleExperimentRunner's KNOWN LIMITATION note
        double aWinRateAsOpponent,  // see BattleExperimentRunner's KNOWN LIMITATION note
        double avgTurnCount
) {
    public void printSummary() {
        System.out.println("=== " + aiAName + " vs " + aiBName + " (" + gamesPlayed + " games) ===");
        System.out.printf("%s: %d wins (%.1f%%) | %s: %d wins (%.1f%%) | Draws: %d (%.1f%%)%n", aiAName, aWins, aWinRate * 100, aiBName, bWins, (double) bWins / gamesPlayed * 100, draws, (double) draws / gamesPlayed * 100);
        System.out.printf("  %s as trainer-slot: %.1f%% | as opponent-slot: %.1f%% (known search-model artifact, not a fairness bug - see BattleExperimentRunner)%n", aiAName, aWinRateAsTrainer * 100, aWinRateAsOpponent * 100);
        System.out.printf("  Avg turns per game: %.1f%n", avgTurnCount);
    }
}