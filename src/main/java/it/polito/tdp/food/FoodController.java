/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	int np;
    	try {
    		np = Integer.parseInt(this.txtPorzioni.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima indica un numero di porzioni intero");
    		return;
    	}
    	
    	model.creaGrafo(np);
    	this.txtResult.appendText("GRAFO CREATO\n\n");
    	this.txtResult.appendText("#vertici: " + model.getNVertici() + "\n");
    	this.txtResult.appendText("#archi: " + model.getNArchi());
    	
    	this.boxFood.getItems().addAll(model.getVertici());
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	
    	int np;
    	try {
    		np = Integer.parseInt(this.txtPorzioni.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima indica un numero di porzioni intero e crea il grafo");
    		return;
    	}
    	
    	Food f = this.boxFood.getValue();
    	if(f == null) {
    		this.txtResult.setText("Prima selezionare un cibo da cui partire");
    		return;
    	}
    	
    	List<Adiacenza> res = model.getCalorie(f);
    	if(!res.isEmpty()) {
    		this.txtResult.appendText("I cibi con più calorie congiunte a " + f + " sono: \n\n");
        	for(int i=0; i< 5; i++) {
        		
        		this.txtResult.appendText(res.get(i) +"\n");
        	}
    	}else {
    		this.txtResult.appendText("Non ci sono vertci congiunti");
    	}
    	
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	
    	int np;
    	try {
    		np = Integer.parseInt(this.txtPorzioni.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima indica un numero di porzioni intero e crea il grafo");
    		return;
    	}
    	
    	Food f = this.boxFood.getValue();
    	if(f == null) {
    		this.txtResult.setText("Prima selezionare un cibo da cui partire");
    		return;
    	}
    	
    	int k;
    	try {
    		k = Integer.parseInt(this.txtK.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima indica un numero K intero");
    		return;
    	}
    	
    	model.init(k, f);
    	model.run();
    	
    	this.txtResult.appendText("Il risultato della simulazione: \n\n");
    	this.txtResult.appendText("Tempo totale: " + String.format("%.2f", model.getTempoTotale()) + " minuti\n");
    	this.txtResult.appendText("Cibi preparati: " + model.getNumeroCibiPreparati());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
