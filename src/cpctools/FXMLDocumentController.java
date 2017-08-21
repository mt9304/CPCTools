/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpctools;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Max
 */
public class FXMLDocumentController implements Initializable
{
    
    @FXML
    private ImageView btn_settings,btn_home;
    @FXML
    private AnchorPane h_settings, h_home;
    
    @FXML
    private void handleButtonAction(MouseEvent event)
    {
        if(event.getTarget() == btn_settings)
        {
            h_settings.setVisible(true);
            h_home.setVisible(false);
        }
        else
            if(event.getTarget() == btn_home)
            {
                h_home.setVisible(true);
                h_settings.setVisible(false);
            }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
}
