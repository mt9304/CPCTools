/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpctools;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.coords.UTMCoord;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//
/**
 *
 * @author Max
 */
public class CPCTools extends Application
{

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        stage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        Scene scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {

        LatLon latLon = UTMCoord.locationFromUTMCoord(10, AVKey.NORTH, 490599.86, 5458794.84);
        double latitude = latLon.getLatitude().degrees;
        double longitude = latLon.getLongitude().degrees;
        
        
        //System.out.println(latitude + " " + longitude);

        /*
        LatLon latLon = UTMCoord.locationFromUTMCoord(10, AVKey.NORTH, 490599.86, 5458794.84);
        double latitude = latLon.getLatitude().degrees;
        double longitude = latLon.getLongitude().degrees;
        System.out.println(latitude + " " + longitude);
        */

        launch(args);
    }

}
