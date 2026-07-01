# Turn-Based Stochastic Battle AI Simulator

A Java simulation engine for turn-based Pokemon style battles, built to compare adversarial search algorithms under realistic stochastic conditions.

## What it does

The engine models a full battle between two Pokemon, including damage variance, move accuracy, critical hits, status conditions (burn, poison, paralyze, sleep, freeze, confusion), and turn order resolved by move priority and speed through a priority queue. Game state is immutable, so every possible move can be explored safely during search without mutating the real battle.

## AI Strategies

Three adversarial search algorithms were implemented and driven by a custom heuristic evaluation function:

- **Minimax with alpha-beta pruning** searches the game tree assuming an optimal opponent.
- **Expectimax** extends this by modeling chance nodes for move accuracy and critical hit probability, rather than assuming worst case outcomes.
- **Monte Carlo Tree Search (MCTS)** with UCB1 selection estimates move value through random rollouts instead of exhaustive search.

The heuristic scores a battle state using HP differential, expected damage output based on type effectiveness, status condition value, and speed advantage.

## Experiment Methodology

Comparing AI strategies fairly required controlling for two sources of bias:

1. **Turn order bias.** Whichever AI is assigned the "trainer" slot in the engine could have a structural advantage independent of skill.
2. **Species bias.** If one AI always controlled a faster or better matched Pokemon, win rate would reflect the Pokemon, not the AI.

To remove both, each matchup runs across four blocks that rotate which AI controls which slot and which Pokemon each AI plays. Across the full set of games, every AI plays the trainer slot exactly as often as the opponent slot, and plays each species exactly as often as the other. This cancels both biases out of the aggregate win rate.

## Results

Each matchup below was run over 4,000 simulated battles (12,000 total).

| Matchup | Win Rate |
|---|---|
| Minimax vs Expectimax | Minimax 52.2% / Expectimax 47.8% |
| Minimax vs MCTS | Minimax 68.8% / MCTS 31.2% |
| Expectimax vs MCTS | Expectimax 64.3% / MCTS 35.8% |

Minimax narrowly outperformed Expectimax and both full search based strategies clearly outperformed MCTS at the simulation budget used.

## Known Limitation

Each AI's search assumes strictly alternating single moves when exploring future states, similar to a chess style search. The actual battle engine resolves both sides' moves within the same turn, ordered by priority and speed. This mismatch produces a reproducible advantage for whichever side the search treats as its own root, visible in the per slot win rate breakdown but not in the aggregate win rate, since every AI plays both slots equally often. This was confirmed at scale rather than assumed, and is documented here rather than fixed, since a full fix would require reworking the search to jointly select both sides' moves per turn instead of assuming alternation, which was judged out of scope given the project's goal of comparing algorithm strength rather than modeling perfect game fidelity.

## Tech Stack

Java, object oriented design, immutable state modeling, adversarial search algorithms, statistical experiment design.
