/**
 * Sample Skeleton for 'Crimes.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.model.Model;
import it.polito.tdp.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CrimesController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Month> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	Integer year = boxAnno.getValue();
    	if (year == null) {
    		txtResult.appendText("Devi selezionare un anno");
    		return;
    	}
    	this.model.creaGrafo(year);
    	
    	for(Integer d : this.model.getDistretti()) {
    		List<Vicino> vicini = this.model.getVicini(d);
    		txtResult.appendText("\nVICINI DI DISTRETTO " + d);
    		for(Vicino v : vicini) {
    			txtResult.appendText("\n" + v.getV() + " d = " + v.getDistanza());
    		}
    	}
    	
    	boxMese.getItems().addAll(this.model.getMonths());
    	boxGiorno.getItems().addAll(this.model.getDays());
    	
    	btnSimula.setDisable(false);

    }

    @FXML
    void doSimula(ActionEvent event) {
    	Integer year = boxAnno.getValue();
    	if (year == null) {
    		txtResult.appendText("Devi selezionare un anno");
    		return;
    	}
    	
    	Month month = boxMese.getValue();
    	if (month == null) {
    		txtResult.appendText("Devi selezionare un mese");
    		return;
    	}
    	
    	Integer day = boxGiorno.getValue();
    	if (day == null) {
    		txtResult.clear();
    		txtResult.appendText("Devi selezionare un giorno");
    		return;
    	}
    	
    	Integer N;
		try {
			N = Integer.parseInt(txtN.getText());
			if (N < 1 || N > 10) {
				txtResult.clear();
	    		txtResult.appendText("Devi inserire un numero tra 1 e 10");
	    		return;
	    	}
		} catch (NumberFormatException e) {
			txtResult.clear();
			txtResult.appendText("Devi inserire un numero tra 1 e 10");
			return;
		}
    	
    	int malGestiti = this.model.simulate(day, month, year, N);
    	txtResult.appendText("Eventi mal gestiti: "+malGestiti);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Crimes.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Crimes.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	boxAnno.getItems().addAll(this.model.getYears());
    	btnSimula.setDisable(true);
    }
}
