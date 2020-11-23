/**
 * Sample Skeleton for 'SerieA.fxml' Controller Class
 */

package it.polito.tdp.seriea;

import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SeasonInt;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class SerieAController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxSquadra"
    private ChoiceBox<Team> boxSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="btnSelezionaSquadra"
    private Button btnSelezionaSquadra; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaAnnataOro"
    private Button btnTrovaAnnataOro; // Value injected by FXMLLoader

    @FXML // fx:id="btnTrovaCamminoVirtuoso"
    private Button btnTrovaCamminoVirtuoso; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;
	
	private Team team = null;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	team = boxSquadra.getValue();
    	if (team == null) {
    		txtResult.appendText("Devi selezionare una squadra");
    		return;
    	}
    	
    	Map<Season, Integer> pointsPerYear = this.model.getPointsPerYear(team);
    	txtResult.clear();
    	txtResult.appendText("Squadra selezionata: "+team.getTeam());
    	txtResult.appendText("\nRisultati ultime stagioni:\n");
    	for (Season s: pointsPerYear.keySet()) {
    		txtResult.appendText(s.getDescription()+" - "+pointsPerYear.get(s)+" punti\n");
    	}
    	
		btnTrovaAnnataOro.setDisable(false);
		btnTrovaCamminoVirtuoso.setDisable(true);
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	if (team == null) {
    		txtResult.appendText("Devi selezionare una squadra");
    		return;
    	}
    	    	
    	this.model.creaGrafo(team);
    	SeasonInt goldenYear = this.model.getGoldYear(team);
    	txtResult.clear();
    	txtResult.appendText("Stagione d'oro "+team.getTeam()+":\n");
    	txtResult.appendText(goldenYear.getSeason().getDescription()+" - "+goldenYear.getI()+" differenza dei pesi");
    	
		btnTrovaCamminoVirtuoso.setDisable(false);
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
    	if (team == null) {
    		txtResult.appendText("Devi selezionare una squadra\n");
    		return;
    	}
    	
    	List<Season> goldenWalk = this.model.findMaxWalk(team);
    	txtResult.clear();
    	txtResult.appendText("Cammino virtuoso "+team.getTeam()+":\n");
    	for (Season s: goldenWalk) {
    		txtResult.appendText(s.getDescription()+"\n");
    	}
   
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
		boxSquadra.getItems().addAll(this.model.getTeams());
		btnTrovaAnnataOro.setDisable(true);
		btnTrovaCamminoVirtuoso.setDisable(true);
		
	}
}
