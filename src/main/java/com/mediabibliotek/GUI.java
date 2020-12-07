package com.mediabibliotek;

import java.awt.MouseInfo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * 
 */
public class GUI extends Application {

	Label theLeftColumn;
	Label theRightColumn;

	TextField theSearchField;
	Button searchButton;
	Button searchBorrowedButton;
	
	Button borrowButton;
	Button sortButton;

	public ListView<String> theTextArea;
	public Label rightLabel;
	String userName;
	
	// Radio buttons   
	RadioButton radioAll; 
	RadioButton radioTitle; 
	RadioButton radioID; 
	ToggleGroup toggleGroup = new ToggleGroup(); 
	
	LibraryController theController;
	
	//methods
	
	public String getValueFromUser() {
		
        TextInputDialog dialog = new TextInputDialog("821223-6666"); //Change this to XXXXXX-XXXX

        dialog.setTitle("Login");
       
        dialog.setHeaderText("Please login using your SSN");
        dialog.setContentText("Enter SSN(Social Security Number): ");

        return dialog.showAndWait().get();
    }
	
	public void alertError(String msg) {
		Alert alert = new Alert(AlertType.CONFIRMATION, msg, ButtonType.OK);
		alert.showAndWait();
	}
	
	/**
	 * Kollar vid inloggningen ifall om en person finns registrerad i Bibliotekssystemet.
	 * @param userName. Personnummer för låntagaren
	 * @return. True om personnummer existerar, annars False.
	 */
	public void login(String user){
		while(true){
			if(theController.checkUserInput(user)){
				if(!theController.checkIfBorrowerExist(user)){
					alertError("Not valid SSN, please try again!");
					user = getValueFromUser();
				}else{
					userName = user;
					break;
				}	
			}else{
				alertError("Not valid SSN, please try again!");
				user = getValueFromUser();
			}	
		}			
	}
	
	//starts to search for media
	public void startSearch() {
		borrowButton.setText("Borrow");
		String theInput = theSearchField.getText();
		if(theController.checkUserInput(theInput)){
			clearTheTextArea();
			if(radioID.isSelected()){
				if(theController.checkInputOnlyDigits(theInput)){
					Media temp = theController.getMedia(theInput);
					theController.mediaSearchResults.add(temp);
					//System.out.println(temp);
					if(temp!=null) setTheTextArea(temp.toString());
				}
			}else if(radioTitle.isSelected()){
				theController.searchMediaTitleByString(theInput);
			}else if(radioAll.isSelected()){
			theController.searchMediaAllByString(theInput);
			}
		}		
	} 

	//clears the listview
	public void clearTheTextArea(){
		theTextArea.getItems().clear();
		rightLabel.setText("");
	}
		
	//adds item/s to the listview
	public void setTheTextArea(String addText){
		//System.out.println(addText);
		theTextArea.getItems().add(addText);

	}
	
	// starts the app
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
				//show library window
				primaryStage.setScene(createScene());
				primaryStage.setTitle("Inloggad som " + userName); // New feature added
		        primaryStage.show();
	}

	public Scene createScene() {
				//variables
				theController = new LibraryController(this);
				
				//login function
//				userName = getValueFromUser();		
//				login(userName);
			
				//layout
			
			// The Main Panel
				BorderPane theMainPane = new BorderPane();
				
				
				Label welcome = new Label("Welcome to the Media Library!");
				welcome.setId("header");

				
				// The Left Panel
				VBox theLeftColumn = new VBox();
				Label leftLabel = new Label("");
				
				Image img = new Image(getClass().getClassLoader().getResource("image.png").toString(), true);
			    ImageView view = new ImageView(img);
			    view.setFitHeight(160);
			    view.setPreserveRatio(true);
			    leftLabel.setGraphic(view);
			    
				searchBorrowedButton = new Button("Search Borrowed");
				searchBorrowedButton.setOnAction(event -> {		
					clearTheTextArea();
					theController.searchBorrowed();
					borrowButton.setText("Return");
				});
				
				borrowButton = new Button("Borrow");
				borrowButton.setOnAction(event -> {
					if(borrowButton.getText().equals("Borrow")){
							String selectedText = theTextArea.getSelectionModel().getSelectedItem();				
							Media selectedMedia = theController.getMediaFromSearchResult(selectedText);					
							if(selectedText!=null && selectedMedia!=null){
								if(selectedMedia.isBorrowed()){
									alertError("Cannot borrow, already borrowed.");
								}else{
									theController.borrowMedia(selectedMedia);
									startSearch();
								}
							}else{
								alertError("Nothing is selected, please click an item in list.");
							}
						}
						else{
							String selectedText = theTextArea.getSelectionModel().getSelectedItem();
							Media selectedMedia = theController.getMediaFromSearchResult(selectedText);
							if(selectedText!=null && selectedMedia!=null){
								if(selectedMedia.isBorrowed()==false)
								{
									alertError("Cannot return, already returned.");	
								}
								else{
									theController.returnMedia(selectedMedia);
									clearTheTextArea();
									theController.searchBorrowed();
								}
							}
							
						} 	
					});
				
			    theLeftColumn.getChildren().add(leftLabel);
			    theLeftColumn.getChildren().add(searchBorrowedButton);
				theLeftColumn.getChildren().add(borrowButton);
				
				// The North Panel
				VBox theNorthPane = new VBox();
				HBox searchFieldBox = new HBox();
				
				theSearchField = new TextField("Please enter your search..."); 
				theSearchField.setOnMouseClicked(e -> theSearchField.selectAll());

				searchButton = new Button("Search");
				searchButton.setOnAction(event -> {
					startSearch();
					//System.out.println(theTextArea.getItems());
				});
				
				theTextArea = new ListView<String>();
				theTextArea.setEditable(false);
				theTextArea.setId("textarea");
				theTextArea.setStyle("-fx-font-alignment: center");
				theTextArea.setOnMouseClicked(event -> rightLabel.setText(theTextArea.getSelectionModel().getSelectedItem())); //New function, let's make the info button obselete
				searchFieldBox.getChildren().add(searchButton);
				searchFieldBox.setAlignment(Pos.BASELINE_RIGHT);
				theNorthPane.getChildren().add(theSearchField);
				theNorthPane.getChildren().add(searchFieldBox);
				theNorthPane.getChildren().add(theTextArea);
				
				
				 //The South Panel  New sorting method implemented
				 HBox theSouthPane = new HBox();
				 sortButton = new Button("Asc");
				 sortButton.setOnAction(event -> {
					clearTheTextArea();
					if (sortButton.getText().equals("Asc")) {
						theController.sortList(true);
						sortButton.setText("Desc");

					}else {
						theController.sortList(false);
						sortButton.setText("Asc");
					}
					 
					 
				 }); 
				 theSouthPane.getChildren().add(sortButton);
				
				// The Right Panel
				VBox theRightColumn = new VBox();
				rightLabel = new Label("");
				rightLabel.setId("rightLabel");
				rightLabel.setWrapText(true);
				theRightColumn.setPrefWidth(300);

				HBox radioButtons = new HBox();
				
				radioButtons.setId("radioButtons");
				radioAll = new RadioButton("All");
				radioAll.setToggleGroup(toggleGroup);
				radioTitle = new RadioButton("Title");
				radioTitle.setToggleGroup(toggleGroup);
				radioID = new RadioButton("ID");
				radioID.setToggleGroup(toggleGroup);
				radioButtons.getChildren().add(radioAll);
				radioAll.setSelected(true);
				radioButtons.getChildren().add(radioTitle);
				radioButtons.getChildren().add(radioID); 
				//toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> System.out.println(newVal + " was selected")); //toggleGroup.getSelectedToggle()

				
				theRightColumn.getChildren().add(radioButtons);
				theRightColumn.getChildren().add(rightLabel);
				
				//build the library window
				theMainPane.setTop(welcome);
				welcome.setAlignment(Pos.TOP_CENTER);
				theMainPane.setCenter(theNorthPane);
				theNorthPane.setAlignment(Pos.TOP_CENTER);
				theMainPane.setLeft(theLeftColumn);
				theLeftColumn.setAlignment(Pos.TOP_CENTER);
				theMainPane.setRight(theRightColumn);
				theMainPane.setBottom(theSouthPane); 	
				theSouthPane.setAlignment(Pos.CENTER);	
				
				Scene scene = new Scene(theMainPane, 900, 600);
				scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
				return scene; 
	}
}