//Author: Yixin Liang  
package songlib.view;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

	
class song_details{
	String song_name, artist_name, album, year;
	
	public song_details(String song, String artist, String albumm, String year) {
	song_name = song;
	artist_name = artist;
	album = albumm;
	this.year = year;
}
}

public class SongController implements Initializable, Comparable<song_details> {
	

	static ObservableList<String> detail_compress ;
	static ObservableList<song_details> detail_observable_list;
	public static int curr_index = 0;
	public static int press_delete_count = 0;
	public static boolean edit_or_not;
	public static int total_item_in_list = 0;
	public static int i_on_Add = 0;

	@FXML ListView<String> listView;

	@FXML Button Add;
	@FXML Button Edit;
	@FXML Button Delete;
	@FXML TextArea textArea;
	public void set_i(int count ){
		i_on_Add++;
	}
	
	public static void add (song_details de, String newVal) {
		
		detail_compress.add(newVal);
		detail_observable_list.add(de);
		
		Collections.sort(detail_compress);
		Comparator<song_details> c = (s1, s2) -> s1.song_name.compareTo(s2.song_name);
		detail_observable_list.sort(c);
		
	}
	
	public static void  set_compression(String oldValue, String newVal) {
		for(int i = 0; i< detail_compress.size(); i++) {
			if(detail_compress.get(i).equals(oldValue)) {
				detail_compress.get(i).replaceAll(oldValue, newVal);
				break;
			}
		}
		Collections.sort(detail_compress);
	}
	
	public static void set_details(String ss, String aa, String al, String yy, int index) {
		song_details new_de = new song_details(ss, aa, al, yy);
		detail_observable_list.set(index, new_de);
		detail_compress.set(index,ss+ " by "+aa);
		Collections.sort(detail_compress);
		Comparator<song_details> c = (s1, s2) -> s1.song_name.compareTo(s2.song_name);
		detail_observable_list.sort(c);
	}
	
	public ObservableList<song_details> get_obserDetails(){
		return detail_observable_list;
	}
	
	public ObservableList<String> get_compress(){
		return detail_compress;
	}
	
	public int get_curr_index() {
		return curr_index;
	}
	    
	public boolean edit_or_not()
	{
		return edit_or_not;
	}
	
 	public void start(Stage mainStage) throws Exception {
 		
 		BufferedReader br = new BufferedReader(new FileReader("storeData.txt"));     
	 		
	 		if (br.readLine() == null ) {
	 			PrintWriter pw = new PrintWriter("storeData.txt");
	 			pw.close();
	 		    str_compress();
	 		    br.close();
	 		}
	 		   
				listView.setPrefHeight(detail_compress.size());
				listView.setMinHeight(0);
				
				listView.getSelectionModel().selectedIndexProperty().addListener((obs,oldVal,newVal) -> setIndex(mainStage));
				
				Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		                listView.requestFocus();
		            }
		        });
				
				if(detail_compress.size() == 0) {
					listView.getSelectionModel().
					selectedIndexProperty().addListener((obs,oldVal,newVal) -> changeArea("", mainStage));
				}
				else {
					int this_index = listView.getSelectionModel().getSelectedIndex();
					String str_area = "  --------Details-------- \nsong name: " + detail_observable_list.get(this_index).song_name
						+ "\nartist: "+ detail_observable_list.get(this_index).artist_name + "\nalbum: "+ detail_observable_list.get(this_index).album
						+ "\nyear: "+ detail_observable_list.get(this_index).year;
				
				    	textArea.setText(str_area);
				    	listView.getSelectionModel().
						selectedIndexProperty().addListener((obs,oldVal,newVal) -> changeArea(str_area, mainStage));
				     }
				    	listView.getSelectionModel().
						selectedIndexProperty().addListener((obs,oldVal,newVal) -> disable_DELETE(mainStage));
				    
				    	if(total_item_in_list < 1)
				    	{
				    		Edit.setDisable(true);
				    		Delete.setDisable(true);
				    	}
				    	else {
							Edit.setDisable(false);
							Delete.setDisable(false);
						}
				    	
				    	Edit.setOnAction(new EventHandler<ActionEvent>() {
							@Override 
							public void handle(ActionEvent e) 
							{
						    	DropShadow shadow = new DropShadow();
						        Edit.setEffect(shadow);
						        FXMLLoader loader = new FXMLLoader();
						        loader.setLocation(getClass().getResource("/songlib/view/songlibScene3.fxml"));
						    	
						        try {
									GridPane childRoot = (GridPane)loader.load();
									Scene scene = new Scene(childRoot, 200, 300);
									mainStage.setScene(scene);
									mainStage.show();
						
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
						       
						    }
						});
 
							Delete.setOnMousePressed((event) -> {
								press_delete_count++;
								listView.setPrefHeight(listView.getPrefHeight()-1);
								total_item_in_list --;
							});
							Delete.setOnMouseReleased((event) -> {
								if(total_item_in_list < 1)
									Delete.setDisable(true);
								else {
									Delete.setDisable(false);
								}
							});
					
							
							Delete.setOnAction(new EventHandler<ActionEvent>() {
								@Override 
								public void handle(ActionEvent e) 
								{
									while(curr_index >0) {
									listView.getSelectionModel().select(listView.getSelectionModel().getSelectedIndex()-1);
									}
									int index = listView.getSelectionModel().getSelectedIndex();
									if (index >= 0) {
										listView.getSelectionModel().
										selectedIndexProperty().addListener((obs,oldVal,newVal) -> changeArea("", mainStage));
//									    listView.getItems().remove(index);
										String str_remoStrin= detail_observable_list.get(index).song_name+"&"+
												detail_observable_list.get(index).artist_name+"&"+
												detail_observable_list.get(index).album+"&"+
												detail_observable_list.get(index).year;
										try {
											removeLine(str_remoStrin);
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										
										detail_compress.remove(index);
									    detail_observable_list.remove(index);
									    listView.setItems(detail_compress);
									    if(total_item_in_list == 0)
									    {
									    	textArea.clear();
									    	textArea.appendText("All items are deleted");
									    	EditController.after_empty_list = false;
									    }
									    else if(total_item_in_list >= 1) {
									    	changeArea(" ", mainStage);
									    	listView.getSelectionModel().select(curr_index + 1);
									    }
									    }
									else 
									{  showItemInputDialog(mainStage); }
								}
							});
				    	
			Add.setOnAction(new EventHandler<ActionEvent>() {
			
				@Override
			    public void handle(ActionEvent e) {
					
			    	DropShadow shadow = new DropShadow();
			        Edit.setEffect(shadow);
			        FXMLLoader loader = new FXMLLoader();
			        loader.setLocation(getClass().getResource("/songlib/view/songlibScene2.fxml"));
			        try {
						GridPane childRoot = (GridPane)loader.load();
						Scene scene = new Scene(childRoot, 200, 300);
						mainStage.setScene(scene);
						mainStage.show();
						
					} catch (IOException e1) 
			        {   e1.printStackTrace();  }
			        
			        e.consume();  
				}
				});
 	}	
 	
	 	
	 	public void removeLine(String lineContent) throws IOException
	 	{
	 	    File file = new File("storeData.txt");
	 	    List<String> out = Files.lines(file.toPath())
	 	                        .filter(line -> !line.contains(lineContent))
	 	                        .collect(Collectors.toList());
	 	    Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	 	}
 		
		@FXML 
		private void disable_DELETE(Stage mainStage) {
			if(total_item_in_list < 1) {
				Delete.setDisable(true);
				Edit.setDisable(true);
			}
			else if(total_item_in_list > 0){
				Delete.setDisable(false);
				Delete.requestFocus();
				Edit.setDisable(false);
				Edit.requestFocus();
			}
		}

	
		@FXML
		public void setIndex(Stage mainStage) {
			Delete.setDisable(false);
			curr_index = listView.getSelectionModel().getSelectedIndex() ;
			listView.scrollTo(curr_index);
			listView.selectionModelProperty();
			Platform.runLater(new Runnable() {

			    @Override
			    public void run() {
			        listView.scrollTo(curr_index);
			        listView.getSelectionModel().select(curr_index);
			    }
			});
		}
		
	   @FXML
	   public void changeArea(String str,Stage maiStage) {
		   
		   if(detail_compress.size()>0 && listView.getSelectionModel().getSelectedIndex()>=0) 
		   {
			   textArea.setText(str);
			   
			   int i = listView.getSelectionModel().getSelectedIndex(); 
			   String albumString =  detail_observable_list.get(i).album;
			   String yearString = detail_observable_list.get(i).year;
			   if(albumString.equals("$")){
				   albumString = "";
			   }
			   if(yearString.equals("$"))
				   yearString = "";
			   if(str.equals("1")) {
				   i= 0;
				   String str_area = "  --------Details-------- \nsong name: " + detail_observable_list.get(i).song_name
					+ "\nartist: "+ detail_observable_list.get(i).artist_name + "\nalbum: "+ albumString + "\nyear: "+ yearString;
				   textArea.setText(str_area);
			   }
			   else {
				   String str_area = "  --------Details-------- \nsong name: " + detail_observable_list.get(i).song_name
					+ "\nartist: "+ detail_observable_list.get(i).artist_name + "\nalbum: "+ albumString + "\nyear: "+ yearString;
				   textArea.setText(str_area);
				   }
			   	maiStage.show();
			   }
	   }
		
	
		 public  void LoadData(String fileName) throws FileNotFoundException{
			try {
	            File f = new File(fileName);
	            Scanner sc = new Scanner(f);
	           
	            if(sc.hasNext()) {
		            while(sc.hasNext()){
		                String line = sc.nextLine();
		                String[] details = line.split("&");
		                String song = details[0];
		                String artist = details[1];
		                String album = details[2];
		                String year = details[3];
		                if(album.equals("$")) {
		                	album = "";
		                }
		                if(year.equals("$")) {
		                	year = "";
		                }
		                song_details each_songDetails = new song_details(song, artist, album, year);
		                detail_observable_list.add(each_songDetails);
		                total_item_in_list ++;
		            }
	            }sc.close();
	            
	        
	            
	        } catch (FileNotFoundException e) {         
	            e.printStackTrace();
	        }
	    }
		 
	
		private void showItemInputDialog(Stage mainStage) {
			int index = listView.getSelectionModel().getSelectedIndex();
			String contentString = "There are nothing to delete.";
			
			TextInputDialog dialog = new TextInputDialog(contentString);
			dialog.initOwner(mainStage);
			dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
		
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()) {
				detail_compress.set(index, result.get());
			}else {
			}
			
		}
		
		private void str_compress(){
			for(int i = 0; i< detail_observable_list.size();i++) {
				String curr_str = detail_observable_list.get(i).song_name + " by " + detail_observable_list.get(i).artist_name;
				detail_compress.add(curr_str);
			}
			listView.setItems(detail_compress);
			}

		void set_new() {
			detail_compress.add("Yay");
			listView.setItems(detail_compress);
		}
		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			detail_compress = FXCollections.observableArrayList();
			detail_observable_list= FXCollections.observableArrayList();
			try {
				LoadData("storeData.txt");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
 			str_compress();
			listView.setItems(detail_compress);
			listView.getSelectionModel().select(0);
			listView.getFocusModel().focus(0);
			listView.scrollTo(0);
			listView.selectionModelProperty();
			Platform.runLater(new Runnable() {

			    @Override
			    public void run() {
			        listView.scrollTo(0);
			        listView.getSelectionModel().select(0);
			    }
			});
			
		}

		@Override
		public int compareTo(song_details o) {
			// TODO Auto-generated method stub
			return 0;
		}
}
			