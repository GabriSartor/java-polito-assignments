/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.db.YearCount;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {
	Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<YearCount> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<String> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void handleAnalizza(ActionEvent event) {
    	String stato = boxStato.getValue();
    	if (stato == null) {
    		txtResult.appendText("Devi selezionare uno stato");
    		return;
    	}
    	
    	List<String> before = this.model.getStatesBefore(stato);
    	List<String> after = this.model.getStatesAfter(stato);
    	List<String> connected = this.model.getConnected(stato);
    	
    	txtResult.clear();
    	txtResult.appendText("Predecessori \n");
    	for(String s : before)
    		txtResult.appendText(s + "\n");
    	
    	txtResult.appendText("\nSuccessori \n");
    	for(String s : after)
    		txtResult.appendText(s + "\n");
    	
    	txtResult.appendText("\nConnessi \n");
    	for(String s : connected)
    		txtResult.appendText(s + "\n");

    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	YearCount y = boxAnno.getValue();
    	if (y==null) {
    		txtResult.appendText("Devi selezionare un anno!");
    		return;
    	}
    	
    	this.model.creaGrafo(y.getYear());
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("# vertici: "+this.model.getNVertex());
    	txtResult.appendText("\t# archi: "+this.model.getNEdge());
    	
    	this.boxStato.getItems().addAll(this.model.getStates());

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	String stato = boxStato.getValue();
    	if (stato == null) {
    		txtResult.appendText("Devi selezionare uno stato");
    		return;
    	}
    	List<String> walk = model.findMaxWalk(stato);
    	
    	txtResult.clear();
    	txtResult.appendText("Sequenza \n");
    	for(String s : walk)
    		txtResult.appendText(s + " - ");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().addAll(model.getYears());
    }
}
