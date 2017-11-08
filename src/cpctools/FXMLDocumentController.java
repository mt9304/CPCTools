/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpctools;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
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

/*
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 */
import com.monitorjbl.xlsx.StreamingReader;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.coords.UTMCoord;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javax.swing.JFileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Max
 */
public class FXMLDocumentController implements Initializable
{

    @FXML
    private ImageView btn_settings, btn_home, btn_convert, btn_map, m_checkmark, c_checkmark;
    @FXML
    private AnchorPane t_settings, t_home, t_convert, t_map;
    @FXML
    private Rectangle m_current_pane;
    @FXML
    private JFXButton btn_browse, btn_convertfile, s_button_browse, btn_convertCSV;
    @FXML
    private Label m_about, l_filename, s_output_path, s_sheetname;
    @FXML
    private JFXProgressBar m_progressbar, c_progressbar;
    @FXML
    private JFXTextField m_progresstext, m_completedtext;
    //@FXML
    //private WebView mc_map;
    @FXML
    private JFXTextArea c_dragArea;

    private WebEngine webengine;
    File selectedFile;
    private String outputDirectory = "C:";
    private String convertedFilename = "ConvertedFile";

    /**
     * For handling bulk csv files. *
     */
    private List<File> bulkCSVFiles;

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
    private void showAbout(MouseEvent event)
    {
        m_about.setVisible(true);
    }

    @FXML
    private void hideAbout(MouseEvent event)
    {
        m_about.setVisible(false);
    }

    @FXML
    private void browseFile(MouseEvent event)
    {
        System.out.println("Pressed Browse. ");
        Node source = (Node) event.getSource();
        Window mainStage = source.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Excel Files", "*.xlsx"),
                new ExtensionFilter("All Files", "*.*"));
        selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null)
        {
            l_filename.setText(selectedFile.getName());
            convertedFilename = selectedFile.getName();
            btn_convertfile.setDisable(false);
            m_progresstext.setVisible(false);
            m_completedtext.setVisible(false);
            m_checkmark.setVisible(false);
        }
    }

    @FXML
    private void selectOutput(MouseEvent event)
    {
        Node source = (Node) event.getSource();
        Window mainStage = source.getScene().getWindow();

        DirectoryChooser outputChooser = new DirectoryChooser();
        outputChooser.setTitle("Select output folder. ");
        File defaultDirectory = new File("C:/");
        outputChooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = outputChooser.showDialog(mainStage);

        if (selectedDirectory != null)
        {
            s_output_path.setText(selectedDirectory.getPath());
            outputDirectory = selectedDirectory.getPath();
        }
    }

    /**
     * For converting single xlsx file. *
     */
    @FXML
    private void convertFile(MouseEvent event) throws IOException, InvalidFormatException, InterruptedException
    {
        System.out.println("Converting file. ");
        m_progressbar.setDisable(false);
        m_progresstext.setStyle("-fx-text-inner-color: white;");
        m_progresstext.setVisible(true);

        ConvertText task = new ConvertText();
        new Thread(task).start();

        //Disable progressbar and display check mark when finished. 
        task.setOnSucceeded(e
                ->
        {
            m_progressbar.setDisable(true);
            m_completedtext.setStyle("-fx-text-inner-color: white;");
            m_completedtext.setVisible(true);
            m_checkmark.setVisible(true);
        });

    }

    //Remember to fix this so its not a bunch of if elses. use ? : and objects. 
    class ConvertText extends Task<Void>
    {

        @Override
        protected Void call() throws Exception
        {
            try ( //Streams the file, so can't seek specific cells, but much faster than opening entire workbook. 
                    InputStream is = new FileInputStream(selectedFile);
                    Workbook workbook = StreamingReader.builder()
                            .rowCacheSize(100)
                            .bufferSize(4096)
                            .open(is))
            {
                Sheet sheet = workbook.getSheetAt(workbook.getNumberOfSheets() - 1);

                //System.out.println("Row: " + sheet.getRow(1));
                //System.out.println(sheet.getSheetName());
                System.out.println(outputDirectory + "\\" + convertedFilename.replace(".xlsx", "") + "-converted" + ".txt");
                outputDirectory = outputDirectory == "C:" ? convertedFilename.replace(".xlsx", "") + "-converted" + ".txt" : outputDirectory + "\\" + convertedFilename.replace(".xlsx", "") + "-converted" + ".txt";
                PrintWriter tdfile = new PrintWriter(new FileWriter(outputDirectory));

                LatLon latLon = UTMCoord.locationFromUTMCoord(10, AVKey.NORTH, 490599.86, 5458794.84);
                double latitude = latLon.getLatitude().degrees;
                double longitude = latLon.getLongitude().degrees;

                int counter = 0;
                String lat = "";
                String lon = "";
                for (Row r : sheet)
                {
                    for (int i = 0; i < 10; i++) //Need to use iterators to get null values between columns. 
                    {
                        Cell c = r.getCell(i);
                        if (c == null || "".equals(c.getStringCellValue()))
                        {
                            System.out.print("N/A" + "\t");
                            tdfile.print("N/A" + "\t");
                            counter++;
                        } else
                        {

                            if (counter % 10 < 8) //If not lat long, then just print value plus tab.  
                            {
                                System.out.print(c.getStringCellValue() + "\t");
                                tdfile.print(c.getStringCellValue() + "\t");
                            } else if (counter % 10 == 8) //If last digit is 8 or 9, then it is lat and long, then convert. 
                            {
                                if ("0".equals(c.getStringCellValue()))
                                {
                                    System.out.print("0" + "\t");
                                    tdfile.print("0" + "\t");
                                } else if ("X".equals(c.getStringCellValue())) //For the header row only no avoid error. 
                                {
                                    System.out.print("X" + "\t");
                                    tdfile.print("X" + "\t");
                                } else
                                {
                                    lat = c.getStringCellValue();
                                }
                            } else if (counter % 10 == 9) //If last digit is 8 or 9, then it is lat and long, then convert. 
                            {
                                if ("0".equals(c.getStringCellValue()))
                                {
                                    System.out.println("0");
                                    tdfile.println("0");
                                } else if ("Y".equals(c.getStringCellValue())) //For the header row only to avoid errors. 
                                {
                                    System.out.println("Y");
                                    tdfile.println("Y");
                                } else
                                {
                                    lon = c.getStringCellValue(); //Getting lat from instance variable. Need to enter both values for conversion function, but can't seek previous value in stream. 
                                    latLon = UTMCoord.locationFromUTMCoord(10, AVKey.NORTH, Double.parseDouble(lat), Double.parseDouble(lon));
                                    latitude = latLon.getLatitude().degrees;
                                    longitude = latLon.getLongitude().degrees;
                                    System.out.println(latitude + "\t" + longitude);
                                    tdfile.println(latitude + "\t" + longitude);
                                }
                            } else
                            {
                                System.out.println("Something went wrong. ");
                            }

                            counter++;
                        }
                    }

                    //Updates the text for how many rows converted. Needs to run in the runLater thing to work, otherwise you get bunch of errors for too many UI changes. 
                    int countertext = counter;
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            m_progresstext.setText("Rows Processed: " + countertext / 10);
                        }
                    });
                }
                tdfile.close();
            }
            return null;
        }
    }

    /**
     * For batch converting csv files. *
     */
    @FXML
    void csvDragDropped(DragEvent event)
    {
        Dragboard board = event.getDragboard();
        List<File> phil = board.getFiles();
        bulkCSVFiles = phil;
        StringBuilder builder = new StringBuilder();
        String fileList = "";
        for (int i = 0; i < phil.size(); i++)
        {
            fileList = fileList + phil.get(i).getName() + "\n";
        }
        c_dragArea.setText(fileList);
    }
    
    @FXML
    void csvBulkConvert(DragEvent event)
    {
        try
        {
            FileInputStream convertfis;
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < bulkCSVFiles.size(); i++)
            {
                convertfis = new FileInputStream(bulkCSVFiles.get(i));
                int ch;
                while ((ch = convertfis.read()) != -1)
                {
                    builder.append((char) ch);
                }
                convertfis.close();
            }
            c_dragArea.setText(builder.toString());
        } 
        catch (FileNotFoundException fnfe)
        {
            fnfe.printStackTrace();
        } 
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
    
    @FXML
    void csvDragEntered(DragEvent event)
    {
        System.out.println("Entered");
        c_dragArea.setText("Drop");
    }

    @FXML
    void csvDragOver(DragEvent event)
    {
        Dragboard board = event.getDragboard();
        if (board.hasFiles())
        {

            List<File> phil = board.getFiles();
            String path = phil
                    .get(0)
                    .toPath()
                    .toString();

            if (path.endsWith(".txt"))
            {
                event
                        .acceptTransferModes(
                                TransferMode.ANY);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb
    )
    {
        btn_convertfile.setDisable(true);
        m_progressbar.setDisable(true);
        m_about.setStyle("-fx-text-inner-color: white;");

        //this.webengine = this.mc_map.getEngine();
        //this.webengine.load("https://www.google.ca/maps");
    }

}
