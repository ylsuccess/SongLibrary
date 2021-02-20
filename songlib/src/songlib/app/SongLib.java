//Author: Yixin Liang 
package songlib.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import songlib.view.SongController;

public class SongLib extends Application {
    
	private static Stage mStage;
	private static Scene mScene;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		mStage = primaryStage;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/songlib.fxml"));
		Parent root = (AnchorPane)loader.load();
		SongController songController = loader.getController();
		songController.start(primaryStage);
		
		Scene scene = new Scene(root, 200, 300);//how about child scene?
		mScene = scene;
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	
	
	public static Stage getStage() {
		return mStage;
	}
	
	public static Scene getScene() {
		return mScene;
	}
	
	public static void main(String[] args) {
			launch(args);
		}
}
