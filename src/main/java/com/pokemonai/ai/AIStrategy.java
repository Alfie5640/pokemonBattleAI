package com.pokemonai.ai;
import com.pokemonai.model.BattleState;
import com.pokemonai.model.Move;
import java.util.List;

// Interface defining the AI contract: given a state and legal moves, return best move
public interface AIStrategy {
    public Move makeDecision(BattleState state, List<Move> moveList);
}
