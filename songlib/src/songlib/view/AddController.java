//Author: Yixin Liang  
package songlib.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import songlib.app.SongLib;

public class AddController {

	@FXML Button add_new;
	@FXML Button Cancel;
	@FXML TextField song_name;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	

	private static String song_add, artist_add, album_add, year_add;
	boolean add_or_not;
	
	@FXML
	private void show_shadow(ActionEvent event) throws IOException {
		
	    Button eButton = (Button)event.getSource();
	    Stage mainStage = SongLib.getStage();
    	//mainStage.setScene(SongLib.getScene());
	    if(year.getText().trim()!= "") {
			if(year.getText().charAt(0) == '-'){
				year.setText("");
				showItem(this, album_add);
			}
		}
	    
	    if(eButton == add_new) {
		    String song_str = song_name.getText().trim();
		    String artist_str = artist.getText().trim();
		    String alb = album.getText().trim();
		    String yeah = year.getText().trim();
		    song_details curr_detailDetails = new song_details(song_str, artist_str, alb, yeah);

		    
			boolean exist_song_name = check_existed_song(song_str, artist_str);
			boolean null_song_artist = check_null_song_artist(song_str, artist_str);
			boolean bar_check = check_bar(song_str, artist_str, alb);
			if(bar_check == false) {
				showItem_invalid(this);
			}
			if(null_song_artist == true) {
				showItem(mainStage);
				mainStage.show();
			}else {
				if(exist_song_name == false) {
					showItem(mainStage, song_str, artist_str);
				}else {
						song_add = curr_detailDetails.song_name;
						artist_add = curr_detailDetails.artist_name;
						album_add = curr_detailDetails.album;
						year_add = curr_detailDetails.year;
						
							try {
								write_to_file(song_str, artist_str, alb, yeah);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							SongController.total_item_in_list ++;
							SongController.i_on_Add++;
							SongController.add(curr_detailDetails, song_add + " by " + artist_add);
							add_or_not=true;
							mainStage = SongLib.getStage();
					    	mainStage.setScene(SongLib.getScene());
					    	mainStage.show();
							
				}
					
				}
	    }
	    else if(eButton == Cancel) {
	    	add_or_not = false;
	    	mainStage = SongLib.getStage();
	    	mainStage.setScene(SongLib.getScene());
	    	mainStage.show();
	    }        
	    
	    
	}

	
	private void showItem_invalid(AddController controller){
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error Message:");
			alert.setHeaderText("Q: Why are you seeing this?");
			String content = "Please Avoid using |";
			alert.setContentText(content);
			song_name.setText("");
			artist.setText("");
			album.setText("");
			alert.showAndWait();
		}
	
	private void showItem(AddController controller, String oldValue) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error Message:");
		alert.setHeaderText("Q: Why are you seeing this?");
		String content = "Year cannot be negative";
		alert.setContentText(content);
		year.setText("");
		alert.showAndWait();
	}
	
	private void showItem(Stage mainStage, String song_name, String artist) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initOwner(mainStage);
		alert.setTitle("Error Message:");
		alert.setHeaderText("Q: Why are you seeing this?");
		String content = song_name +" by "+ artist + " already in list.";
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	private void showItem(Stage mainStage) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(mainStage);
			alert.setTitle("Error Message");
			alert.setHeaderText("Q: Why are you seeing this?");
			String content = "To save, song name and artist info are required.";
			alert.setContentText(content);
			alert.showAndWait();	
		}
	
	
	public static boolean check_existed_song(String songName, String artistName) {
		
		for(song_details i: SongController.detail_observable_list) {
			if(i.song_name.equals(songName) && i.artist_name.equals(artistName))
				return false;//false meaning both the artist and song name exist
		}
		return true;
	}
	
	public static boolean check_bar(String songName, String artistName, String album) {
		int count = 0;
		for(int i = 0; i< songName.length(); i++) {
			if(songName.charAt(i) == '|') {
				count ++;
			break;
			}
		}
		for(int i = 0; i< artistName.length(); i++) {
			if(artistName.charAt(i) == '|') {
				count++;
				break;
			}
		}
		for(int i = 0; i< album.length(); i++) {
			if(album.charAt(i) == '|') {
				count++;
				break;}
		}
		if(count >0) {
			return false;
		}
		return true;
	}
	
	private boolean check_null_song_artist(String songName, String artist) {
		if(songName == null || songName.trim().isEmpty()|| artist == null || artist.trim().isEmpty()) {
			return true;//true meaning either of them is null
		}
		return false;
	}
	
	 
		private static void write_to_file(String song, String art, String album, String year) throws IOException {
			
			FileWriter writer = new FileWriter("storeData.txt", true);
			String curr_album = album;
			String curr_year = year;
			if(album == "") {
				curr_album = "$";
			}
			if(year == "")
			{
				curr_year = "$";
			}
			String str = song + "&" + art + "&" + curr_album + "&" + curr_year + "\n";
			BufferedWriter bWriter = new BufferedWriter(writer);
			
			
			bWriter.write(str);
			bWriter.close();
			writer.close();
			
			sort_file();
		}
		
		private static void sort_file() throws IOException
		{
			BufferedReader reader = new BufferedReader(new FileReader("storeData.txt"));
			ArrayList<String> str = new ArrayList<>();
			String line = "";
			while((line=reader.readLine())!=null){
					str.add(line);
			}
			reader.close();
			Collections.sort(str);
			FileWriter writer2 = new FileWriter("storeData.txt");
			for(String s: str){
					writer2.write(s);
					writer2.write("\r\n");
			}
			writer2.close();
			
		}
		
		
	    
}
