package put.ai.games.GrajaMateusz;

import java.util.List;
import java.util.Random;

import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;
import put.ai.games.game.moves.SkipMove;

public class GrajaMateusz extends Player {

    private static final int MAX_DEPTH = 3;

    private final Random random = new Random();

    @Override
    public String getName() {
        return "Mateusz Graja 155901 Piotr Zurek 155977";
    }

    @Override
    public Move nextMove(Board board) {
        long startTime = System.currentTimeMillis();
        long endTime   = startTime + getTime() - 50;

        List<Move> moves = board.getMovesFor(getColor());
        if (moves.isEmpty()) {
            return movesOrSkip(moves);
        }

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

        if (bestMove == null) {
            bestMove = moves.get(random.nextInt(moves.size()));
        }

        return bestMove;
    }

    private double alphaBeta(Board board, int depth,
                             double alpha, double beta,
                             boolean maximizingPlayer,
                             long endTime) {

        if (System.currentTimeMillis() >= endTime) {
            return evaluate(board);
        }

        Color winner = board.getWinner(currentPlayerColor(maximizingPlayer));
        if (winner != null) {
            if (winner == getColor()) {
                return Double.POSITIVE_INFINITY;
            } else if (winner == Color.EMPTY) {
                return 0.0;
            } else {
                return Double.NEGATIVE_INFINITY;
            }
        }

        if (depth <= 0) {
            return evaluate(board);
        }

        Color movingColor = currentPlayerColor(maximizingPlayer);
        List<Move> moves = board.getMovesFor(movingColor);
        if (moves.isEmpty()) {
            return evaluate(board);
        }

        if (maximizingPlayer) {
            double value = Double.NEGATIVE_INFINITY;
            for (Move m : moves) {
                board.doMove(m);
                double score = alphaBeta(board, depth - 1, alpha, beta,
                        false, endTime);
                board.undoMove(m);

                if (score > value) {
                    value = score;
                }
                if (value > alpha) {
                    alpha = value;
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return value;
        } else {
            double value = Double.POSITIVE_INFINITY;
            for (Move m : moves) {
                board.doMove(m);
                double score = alphaBeta(board, depth - 1, alpha, beta,
                        true, endTime);
                board.undoMove(m);

                if (score < value) {
                    value = score;
                }
                if (value < beta) {
                    beta = value;
                }
                if (beta <= alpha) {
                    break;
                }
            }
            return value;
        }
    }

    private double evaluate(Board board) {
        // Liczymy piony nasze i przeciwnika
        int myCount  = countStones(board, getColor());
        int oppCount = countStones(board, Player.getOpponent(getColor()));
        return myCount - oppCount;
    }

    private int countStones(Board board, Color color) {
        int count = 0;
        int size  = board.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.getState(x, y) == color) {
                    count++;
                }
            }
        }
        return count;
    }

    private Color currentPlayerColor(boolean maximizingPlayer) {
        return maximizingPlayer ? getColor() : Player.getOpponent(getColor());
    }

    private Move movesOrSkip(List<Move> moves) {
        if (moves.isEmpty()) {
            return new SkipMove() {
                @Override
                public Color getColor() {
                    return getColor();
                }
            };
        }
        return moves.get(0);
    }
}
