import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TicTacToe extends Application {
	int size = 450;
	int[][] matrix = new int[3][3];
	//Alternates between true and false to denote player 1 and 2 respectively
	boolean turn = true;
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		ImageView[][] imageViews = new ImageView[3][3];
		
		StackPane[][] stackPanes = new StackPane[3][3];
		
		GridPane mainGridPane = new GridPane();
		mainGridPane.setPrefWidth(size);
		mainGridPane.setPrefHeight(size);
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				imageViews[i][j] = new ImageView();
				imageViews[i][j].setFitHeight(size / 3);
				imageViews[i][j].setFitWidth(size / 3);
				
				stackPanes[i][j] = new StackPane();
				stackPanes[i][j].getChildren().add(imageViews[i][j]);
				stackPanes[i][j].setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				stackPanes[i][j].setId(i + "," + j);
				
				stackPanes[i][j].setOnMouseClicked(e -> {
					int i2 = Integer.parseInt(((StackPane) e.getSource()).getId().split(",")[0]);
					int j2 = Integer.parseInt(((StackPane) e.getSource()).getId().split(",")[1]);
					
					if(matrix[i2][j2] == 0) {
						if(turn) {
							imageViews[i2][j2].setImage(new Image(getClass().getResource("/o.png").toString()));
							
							matrix[i2][j2] = 1;
						} else {
							imageViews[i2][j2].setImage(new Image(getClass().getResource("/x.png").toString()));
							
							matrix[i2][j2] = 2;
						}
						
						int winner = checkForWinner();
						
						if(winner > 0) {
							ButtonType playAgainButtonType = new ButtonType("Play again", ButtonData.BACK_PREVIOUS);
							ButtonType quitButtonType = new ButtonType("Quit", ButtonData.OK_DONE);
							
							Optional<ButtonType> result = QuickAlert.showCustomButtons(AlertType.INFORMATION, "Congratulations!", "Player " + winner + " wins!", true, playAgainButtonType, quitButtonType);
							
							if(result.isPresent()) {
								if(result.get().getButtonData() == ButtonData.BACK_PREVIOUS) {
									for(int i3 = 0; i3 < 3; i3++) {
										for(int j3 = 0; j3 < 3; j3++) {
											imageViews[i3][j3].setImage(null);
											matrix[i3][j3] = 0;
										}
									}
									
									return;
								} else {
									System.exit(0);
								}
							}
						}
						
						if(winner == -1) {
							ButtonType playAgainButtonType = new ButtonType("Play again", ButtonData.BACK_PREVIOUS);
							ButtonType quitButtonType = new ButtonType("Quit", ButtonData.OK_DONE);
							
							Optional<ButtonType> result = QuickAlert.showCustomButtons(AlertType.INFORMATION, "No winner!", "Try again?", true, playAgainButtonType, quitButtonType);
							
							if(result.isPresent()) {
								if(result.get().getButtonData() == ButtonData.BACK_PREVIOUS) {
									for(int i3 = 0; i3 < 3; i3++) {
										for(int j3 = 0; j3 < 3; j3++) {
											imageViews[i3][j3].setImage(null);
											matrix[i3][j3] = 0;
										}
									}
									
									return;
								} else {
									System.exit(0);
								}
							}
						}
						
						turn = !turn;
					}
				});
				
				mainGridPane.add(stackPanes[i][j], j, i);
			}
		}
		
		Scene scene = new Scene(mainGridPane);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("PackrGUI");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	private int checkForWinner() {
		//D-1 = row
		//D-2 = column
		
		//diagonal top left to bottom right
		if(matrix[0][0] == matrix[1][1] && matrix[1][1] == matrix[2][2]) {
			return matrix[0][0];
		}
		
		//diagonal top right to bottom left
		if(matrix[0][2] == matrix[1][1] && matrix[1][1] == matrix[2][0]) {
			return matrix[0][2];
		}
		
		//horizontal top
		if(matrix[0][0] == matrix[0][1] && matrix[0][1] == matrix[0][2]) {
			return matrix[0][0];
		}
		
		//horizontal middle
		if(matrix[1][0] == matrix[1][1] && matrix[1][1] == matrix[1][2]) {
			return matrix[1][0];
		}
		
		//horizontal bottom
		if(matrix[2][0] == matrix[2][1] && matrix[2][1] == matrix[2][2]) {
			return matrix[2][0];
		}
		
		//vertical left
		if(matrix[0][0] == matrix[1][0] && matrix[1][0] == matrix[2][0]) {
			return matrix[0][0];
		}
		
		//vertical middle
		if(matrix[0][1] == matrix[1][1] && matrix[1][1] == matrix[2][1]) {
			return matrix[0][1];
		}
		
		//vertical right
		if(matrix[0][2] == matrix[1][2] && matrix[1][2] == matrix[2][2]) {
			return matrix[0][2];
		}
		
		//Return 0 (no winner) if there are any blank squares
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(matrix[i][j] == 0) {
					return 0;
				}
			}
		}
		
		//Return -1 (board is full)
		return -1;
	}
}
