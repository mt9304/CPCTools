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
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Max
 */
public class FXMLDocumentController implements Initializable
{
    
    @FXML
    private ImageView btn_settings,btn_home, btn_convert, btn_map;
    @FXML
    private AnchorPane h_settings, h_home, h_convert, h_map;
    @FXML
    private Rectangle m_current_pane;
    
    //Handles changing panes for the main menu. 
    @FXML
    private void handleButtonAction(MouseEvent event)
    {
        if(event.getTarget() == btn_settings)
        {
            h_settings.setVisible(true);
            h_home.setVisible(false);
            h_convert.setVisible(false);
            h_map.setVisible(false);
            m_current_pane.setLayoutX(1060);
        }
        else
            if(event.getTarget() == btn_home)
            {
                h_home.setVisible(true);
                h_settings.setVisible(false);
                h_convert.setVisible(false);
                h_map.setVisible(false);
                m_current_pane.setLayoutX(113);
            }
        else
            if(event.getTarget() == btn_convert)
            {
                h_convert.setVisible(true);
                h_settings.setVisible(false);
                h_home.setVisible(false);
                h_map.setVisible(false);
                m_current_pane.setLayoutX(431);
            }
        else
            if(event.getTarget() == btn_map)
            {
                h_map.setVisible(true);
                h_convert.setVisible(false);
                h_settings.setVisible(false);
                h_home.setVisible(false);
                m_current_pane.setLayoutX(753);
            }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
}
