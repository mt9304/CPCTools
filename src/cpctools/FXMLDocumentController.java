/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpctools;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private JFXButton btn_browse, btn_convertfile;
    @FXML
    private Label l_filename;
    @FXML
    private JFXProgressBar m_progressbar;

    File selectedFile;

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
                new ExtensionFilter("Excel Files", "*.xlsx"),
                new ExtensionFilter("All Files", "*.*"));
        selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null)
        {
            l_filename.setText(selectedFile.getName());
            btn_convertfile.setDisable(false);

        }
    }

    @FXML
    private void convertFile(MouseEvent event) throws IOException, InvalidFormatException
    {
        System.out.println("Converting file. ");
        m_progressbar.setDisable(false);

        //Remember to make sense of this. Maybe just use file input stream in the browseFile function instead of delcaring so much. 
        //Also, need to add as many jars needed, xmlbeans.2.6.0 too. 
        //InputStream ExcelFileToRead = new FileInputStream(selectedFile);
        //XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
        //XSSFWorkbook wb = new XSSFWorkbook(selectedFile);
        //System.out.println(wb.getSheetAt(0).getRow(0).getCell(0));
        //System.out.println(wb.getSheetAt(1).getRow(1).getCell(1));
        try (
                InputStream is = new FileInputStream(selectedFile);
                Workbook workbook = StreamingReader.builder()
                        .rowCacheSize(100)
                        .bufferSize(4096)
                        .open(is))
        {
            Sheet sheet = workbook.getSheet("Sheet2");
            
                System.out.println(sheet.getSheetName());
                for (Row r : sheet)
                {
                    for (Cell c : r)
                    {
                        System.out.println(c.getStringCellValue());
                    }
                }
            
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        btn_convertfile.setDisable(true);
        m_progressbar.setDisable(true);
    }

}
