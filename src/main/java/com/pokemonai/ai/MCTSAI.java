package com.pokemonai.ai;

import com.pokemonai.model.BattleState;
import com.pokemonai.model.Move;
import com.pokemonai.model.Pokemon;

import java.util.*;

public class MCTSAI implements AIStrategy {

    private static final int SIMULATIONS = 200;
    private static final double EXPLORATION = Math.sqrt(2);
    private static final Random RANDOM = new Random();

    @Override
    public Move makeDecision(BattleState state, List<Move> moveList) {

        MCTSNode root = new MCTSNode(state, null, null);

        for (int i = 0; i < SIMULATIONS; i++) {
            MCTSNode node = select(root);
            node = expand(node);
            double result = simulate(node.state);
            backpropagate(node, result);
        }

        return bestMove(root);
    }

    private MCTSNode select(MCTSNode node) {

        while (!node.isTerminal() && node.isFullyExpanded()) {
            node = bestUCB(node);
        }
        return node;
    }

    private MCTSNode expand(MCTSNode node) {

        if (node.isTerminal()) return node;

        Move move = node.getUntriedMove();
        if (move == null) return node;

        BattleState nextState = node.state.applyMoveForSearch(move);
        MCTSNode child = new MCTSNode(nextState, node, move);

        node.children.add(child);
        node.untriedMoves.remove(move);

        return child;
    }

    private double simulate(BattleState state) {

        BattleState current = state;
        int depth = 10;

        while (depth > 0 && !current.getTrainerPokemon().isFainted() && !current.getOpponentPokemon().isFainted()) {

            List<Move> moves = movesFor(current);
            Move move = moves.get(RANDOM.nextInt(moves.size()));
            current = current.applyMoveForSearch(move);

            depth--;
        }

        return Heuristic.evaluateState(current);
    }

    private void backpropagate(MCTSNode node, double result) {
        while (node != null) {
            node.visits++;
            node.totalScore += result;
            node = node.parent;
        }
    }

    private Move bestMove(MCTSNode root) {
        MCTSNode best = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        for (MCTSNode child : root.children) {
            double value = child.totalScore / child.visits;

            if (value > bestValue) {
                bestValue = value;
                best = child;
            }
        }

        return best.move;
    }

    private MCTSNode bestUCB(MCTSNode node) {
        MCTSNode best = null;
        double bestValue = Double.NEGATIVE_INFINITY;

        for (MCTSNode child : node.children) {
            double avgScore = child.totalScore / (child.visits + 1);
            double exploit = node.trainerToMove ? avgScore : -avgScore;
            double explore = EXPLORATION * Math.sqrt(Math.log(node.visits + 1) / (child.visits + 1));

            double ucb = exploit + explore;

            if (ucb > bestValue) {
                bestValue = ucb;
                best = child;
            }
        }

        return best;
    }

    private static List<Move> movesFor(BattleState state) {
        Pokemon actor = state.isTrainersTurn() ? state.getTrainerPokemon() : state.getOpponentPokemon();
        return actor.getMoveList();
    }

    private static class MCTSNode {

        BattleState state;
        MCTSNode parent;
        List<MCTSNode> children = new ArrayList<>();
        List<Move> untriedMoves;
        Move move;

        double totalScore = 0;
        int visits = 0;

        // Whose turn it is to choose a move from this state
        boolean trainerToMove;

        MCTSNode(BattleState state, MCTSNode parent, Move move) {
            this.state = state;
            this.parent = parent;
            this.move = move;

            this.trainerToMove = state.isTrainersTurn();
            this.untriedMoves = new ArrayList<>(movesFor(state));
        }

        boolean isFullyExpanded() {
            return untriedMoves.isEmpty();
        }

        boolean isTerminal() {
            return state.getTrainerPokemon().isFainted() || state.getOpponentPokemon().isFainted();
        }

        Move getUntriedMove() {
            if (untriedMoves.isEmpty()) return null;
            return untriedMoves.getFirst();
        }
    }
}