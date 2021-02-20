//Author: Yixin Liang  
package songlib.view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import songlib.app.SongLib;

public class EditController implements Initializable{

	@FXML Button finish_change;
	@FXML Button Cancel2;
	@FXML TextField song_name_edit;
	@FXML TextField artist_edit;
	@FXML TextField album_edit;
	@FXML TextField year_edit;
	@FXML Button Finish2;
	public static boolean after_empty_list = false;
	
	@FXML
	private void edit_scene(ActionEvent event) throws IOException {
	
		String ss = SongController.detail_observable_list.get(SongController.curr_index).song_name;
		String aa = SongController.detail_observable_list.get(SongController.curr_index).artist_name;
		String al = SongController.detail_observable_list.get(SongController.curr_index).album;
		String yy = SongController.detail_observable_list.get(SongController.curr_index).year;
		boolean check_bar = check_bar(aa, yy, al);
		if(check_bar == false) {
			showItem_invalid();
			Stage mainStage = SongLib.getStage();
	    	mainStage.setScene(SongLib.getScene());
	    	mainStage.show();
			
		}
		if(al.equals("$")) {
			al = "";
		}
		if(yy.equals("$")) {
			yy = "";
		}
		String old_year_valString = yy;
	    Button eButton = (Button)event.getSource();
	    song_name_edit.setFocusTraversable(false);
    	if(eButton == finish_change)
    	{
    		Finish2.setDisable(false);
    		song_name_edit.setText(ss);
		    artist_edit.setText(aa);
		    album_edit.setText(al);
		    year_edit.setText(yy);
		    finish_change.setText("Finish");
		    finish_change.setVisible(false);
		    Finish2.setVisible(true);
		    song_name_edit.setFocusTraversable(false);
		    SongController.edit_or_not = true;
		    if(year_edit.getText()!= "") {
			    if(year_edit.getText().charAt(0)== '-') {
					showItem(this, old_year_valString);
				}
		    }
    	}else if(eButton == Cancel2) {
			Stage mainStage = SongLib.getStage();
	    	mainStage.setScene(SongLib.getScene());
		}else if(eButton == Finish2){
			boolean yes_or_no = AddController.check_existed_song(song_name_edit.getText(), artist_edit.getText());
				if(yes_or_no == false && !(al.equals(album_edit.getText()) && !(yy.equals(year_edit.getText())))) {
					showItem();
					Stage mainStage = SongLib.getStage();
			    	mainStage.setScene(SongLib.getScene());
			    	 mainStage.show();
				}
					 String yString;
					 if(year_edit.getText().charAt(0) == '-') {
						 yString = old_year_valString;showItem(this, old_year_valString
								 );
					 }
					 else {
						yString = year_edit.getText();
					}
		    		SongController.edit_or_not = true;
		    		
		    		write_whole_file(SongController.detail_observable_list);
		    		SongController.set_compression(ss, song_name_edit.getText());
		    		SongController.set_details(song_name_edit.getText(), artist_edit.getText(), album_edit.getText(),yString,SongController.curr_index);
		    		after_empty_list = false;
					Stage mainStage = SongLib.getStage();
			    	mainStage.setScene(SongLib.getScene());
			    	 mainStage.show();
				 }
			

	}
	
	private void showItem(EditController controller, String oldValue) {
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Message:");
			alert.setHeaderText("Q: Why are you seeing this?");
			String content = "Year cannot be negative";
			alert.setContentText(content);
			year_edit.setText(oldValue);
			alert.showAndWait();
		}
	
	private void showItem() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error Message:");
		alert.setHeaderText("Q: Why are you seeing this?");
		String content = "Existed song and artist";
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	private void showItem_invalid(){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error Message:");
		alert.setHeaderText("Q: Why are you seeing this?");
		String content = "Please Avoid using |";
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	
	public static boolean check_bar(String songName, String artistName, String album) {
		int count = 0;
		for(int i = 0; i< songName.length(); i++) {
			if(songName.charAt(i) == '|')
				count ++;
		}
		for(int i = 0; i< artistName.length(); i++) {
			if(artistName.charAt(i) == '|')
				count++;
		}
		for(int i = 0; i< album.length(); i++) {
			if(album.charAt(i) == '|')
				count++;
		}
		if(count >0) {
			return false;
		}
		return true;
	}
	
	public static void write_whole_file(ObservableList<song_details> detail_obs) throws IOException {
		
		PrintWriter pw = new PrintWriter("storeData.txt");
		pw.close();
		
		FileWriter writer = new FileWriter("storeData.txt", true);
		BufferedWriter bWriter = new BufferedWriter(writer);
		int i = 0;
		while(i< detail_obs.size())
		{
			String curr_album = detail_obs.get(i).album;
			String curr_year = detail_obs.get(i).year;
			if(detail_obs.get(i).album == "") {
				curr_album = "$";
			}
			if(detail_obs.get(i).year == "")
			{
				curr_year = "$";
			}
			String str = detail_obs.get(i).song_name + "&" + detail_obs.get(i).artist_name + "&" + curr_album + "&" + curr_year + "\n";
			bWriter.write(str);
		    i++;
		    
		}
		
			bWriter.close();
			writer.close();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Finish2.setVisible(false);
	}
}
