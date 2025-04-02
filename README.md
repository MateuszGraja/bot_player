# Alpha-Beta AI Player for PUT Board Game

---

## Project Description

This project implements an AI player using the **Alpha-Beta Pruning Minimax algorithm** for a board game framework used at Poznan University of Technology. The AI analyzes possible future moves to determine the most optimal action, prioritizing performance through pruning and time constraints.

## Authors

**Mateusz Graja** (155901)  
**Piotr Żurek** (155977)

## Key Features

- **Alpha-Beta Pruning**: Efficient decision-making by eliminating branches that do not need to be evaluated.
- **Depth-Limited Search**: Configurable lookahead with a max search depth of 3.
- **Time-Constrained Evaluation**: Aborts deep evaluation if time limit is about to be exceeded.
- **Simple Evaluation Function**: Evaluates board state based on the difference in the number of pieces.
- **Fallback Logic**: Chooses a random legal move when no optimal move is found.

## How It Works

### 1. Main Entry Point

The `nextMove()` method evaluates all possible moves and chooses the one with the best score:

```java
Move bestMove = null;
double bestValue = Double.NEGATIVE_INFINITY;

for (Move m : moves) {
    board.doMove(m);
    double value = alphaBeta(board, MAX_DEPTH - 1,
                             Double.NEGATIVE_INFINITY,
                             Double.POSITIVE_INFINITY,
                             false, endTime);
    board.undoMove(m);

    if (value > bestValue) {
        bestValue = value;
        bestMove  = m;
    }
}
```

### 2. Alpha-Beta Algorithm

The recursive `alphaBeta()` method applies pruning and alternates between maximizing and minimizing player:

```java
if (maximizingPlayer) {
    for (Move m : moves) {
        board.doMove(m);
        double score = alphaBeta(...);
        board.undoMove(m);
        if (score > value) value = score;
        alpha = Math.max(alpha, value);
        if (alpha >= beta) break;  // Beta cut-off
    }
} else {
    for (Move m : moves) {
        board.doMove(m);
        double score = alphaBeta(...);
        board.undoMove(m);
        if (score < value) value = score;
        beta = Math.min(beta, value);
        if (beta <= alpha) break;  // Alpha cut-off
    }
}
```

### 3. Evaluation Function

Estimates board advantage as the difference in number of pieces:

```java
private double evaluate(Board board) {
    int myCount  = countStones(board, getColor());
    int oppCount = countStones(board, Player.getOpponent(getColor()));
    return myCount - oppCount;
}
```

### 4. Game Outcome Handling

When a winner is detected, return fixed values for win, loss, or draw:

```java
if (winner == getColor()) {
    return Double.POSITIVE_INFINITY;
} else if (winner == Color.EMPTY) {
    return 0.0;
} else {
    return Double.NEGATIVE_INFINITY;
}
```

## Usage Notes

- The algorithm supports skipping moves via `SkipMove` in case no legal move is available.
- The time margin of 50ms ensures the AI returns before timeout.

## Potential Improvements

- Add heuristic evaluation for strategic advantage (e.g., piece mobility, center control).
- Introduce iterative deepening to better manage time.
- Parallelize move evaluation for faster decision-making.

---

This project demonstrates a clean, effective implementation of Alpha-Beta pruning in a real-world board game AI. Designed for both performance and readability.
