package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class OthelloUI extends Control {
    //connect with view and controller
    @FXML TextArea BlackNum;
    @FXML TextArea WhiteNum;
    @FXML Text GameState;
    @FXML AnchorPane gamePane;
    @FXML AnchorPane infoPane;
    Board board;
    int i,j;
    Circle[][] stones;
    boolean Started = false;

    public void gameStart() {
        if(!Started) {
            board = new Board();
            stones = new Circle[8][8];
            newTurn();
            applyBoard();
            Started = true;
        }
        else {
            DestroyBoard();
            Started = false;
            gameStart();
        }
    }

    public void applyBoard() {
        for(int i = 0; i < board.boardSize; i++) {
            for(int j = 0; j < board.boardSize; j++) {
                if(board.board[i][j] != board.EMPTY) {
                    if(board.board[i][j] == board.BLACK) {
                        gamePane.getChildren().remove(stones[i][j]);
                        stones[i][j] = new Circle(155 + 90 * j, 85 + 90 * i, 40);
                        stones[i][j].setFill(Color.BLACK);
                        gamePane.getChildren().add(stones[i][j]);
                    }
                    else if(board.board[i][j] == board.WHITE) {
                        gamePane.getChildren().remove(stones[i][j]);
                        stones[i][j] = new Circle(155 + 90 * j, 85 + 90 * i, 40);
                        stones[i][j].setFill(Color.WHITE);
                        gamePane.getChildren().add(stones[i][j]);
                    }
                }
            }
        }
    }

    public void setMouseAction() {
        gamePane.setOnMouseClicked(event -> { // 람다 사용.
            if(event.getSceneX() < 110 || event.getSceneX() > 830 ||
                    event.getSceneY() < 40 || event.getSceneY() > 760) {
                return;
            }
            for(i = 0; i < board.boardSize; i++) {
                if(event.getSceneX() <= 200 + 90*i)
                    break;
            }
            for(j = 0; j < board.boardSize; j++) {
                if(event.getSceneY() <= 130 + 90*j)
                    break;
            }

            if(board.board[j][i] != board.EMPTY)
                return;
            else {
                board.board[j][i] = board.player;

                if(!board.determineReverse(j,i)) {
                    board.board[j][i] = board.EMPTY;
                    return;
                }
            }
            board.reverse(j,i);
            applyBoard();
            board.swapPlayer();
            newTurn();
            board.isGameEnd();
            newTurn();
        });
    }

    public void newTurn() {
        if(board.player == board.BLACK) {
            GameState.setText("[흑의 차례]");
            BlackNum.setText(board.countBlack());
            WhiteNum.setText(board.countWhite());
        }

        else if(board.player == board.WHITE) {
            GameState.setText("[백의 차례]");
            BlackNum.setText(board.countBlack());
            WhiteNum.setText(board.countWhite());
        }

    }


    public void DestroyBoard() {
        for(int x = 0; x < board.boardSize; x++) {
            for(int y = 0; y < board.boardSize; y++) {
                if(stones[x][y] != null)
                    gamePane.getChildren().remove(stones[x][y]);
            }
        }
        board.player = board.BLACK;
    }
}
