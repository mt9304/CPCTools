/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpctools;

import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 *
 * @author Max
 */
public class FXMLDocumentController implements Initializable
{

    @FXML
    private ImageView btn_settings, btn_home, btn_convert, btn_map;
    @FXML
    private AnchorPane t_settings, t_home, t_convert, t_map;
    @FXML
    private Rectangle m_current_pane;
    @FXML
    private JFXButton btn_browse;
    @FXML
    private Label l_filename;

    //Handles changing panes for the main menu. 
    @FXML
    private void handleButtonAction(MouseEvent event)
    {
        if (event.getTarget() == btn_settings)
        {
            t_settings.setVisible(true);
            t_home.setVisible(false);
            t_convert.setVisible(false);
            t_map.setVisible(false);
            m_current_pane.setLayoutX(1060);
        } else if (event.getTarget() == btn_home)
        {
            t_home.setVisible(true);
            t_settings.setVisible(false);
            t_convert.setVisible(false);
            t_map.setVisible(false);
            m_current_pane.setLayoutX(113);
        } else if (event.getTarget() == btn_convert)
        {
            t_convert.setVisible(true);
            t_settings.setVisible(false);
            t_home.setVisible(false);
            t_map.setVisible(false);
            m_current_pane.setLayoutX(431);
        } else if (event.getTarget() == btn_map)
        {
            t_map.setVisible(true);
            t_convert.setVisible(false);
            t_settings.setVisible(false);
            t_home.setVisible(false);
            m_current_pane.setLayoutX(753);
        }
    }

    @FXML
    private void browseFile(MouseEvent event)
    {
        
            System.out.println("Pressed Browse. ");
            Node source = (Node) event.getSource();
            Window mainStage = source.getScene().getWindow();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Text Files", "*.txt"),
                    new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                    new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                    new ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(mainStage);
            if (selectedFile != null)
            {
                //mainStage.display(selectedFile);
                l_filename.setText(selectedFile.getName());
            }
        

    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

}
