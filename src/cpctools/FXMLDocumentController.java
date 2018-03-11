package cpctools;
import com.jfoenix.controls.JFXButton;
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
import com.monitorjbl.xlsx.StreamingReader;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.coords.UTMCoord;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class FXMLDocumentController implements Initializable
{
    @FXML
    private ImageView btn_settings, btn_home, btn_xslx, btn_csv, btn_exit, xlsx_checkmark, csv_checkmark;
    @FXML
    private AnchorPane t_settings, t_home, t_xslx, t_csv; //Tab panes. 
    @FXML
    private Rectangle current_tab_indicator;
    @FXML
    private JFXButton xlsx_browse_btn, xlsx_convert_btn, settings_browse_btn, csv_convert_btn;
    @FXML
    private Label home_about_details, xlsx_filename, settings_output_path, settings_sheetname;
    @FXML
    private JFXTextField xlsx_progress_text, xlsx_completed_text, csv_completed_text;
    @FXML
    private Hyperlink home_hyperlink;
    @FXML
    private JFXTextArea csv_drag_area;
    
    File selectedFile;
    private String outputDirectory = "C:";
    private String convertedFilename = "ConvertedFile";

    /**
     * For handling bulk csv files. *
     */
    private List<File> bulkCSVFiles;

    //Handles changing panes for the main menu. 
    @FXML
    private void switchTabs(MouseEvent event)
    {
        if (event.getTarget() == btn_settings)
        {
            t_settings.setVisible(true);
            t_home.setVisible(false);
            t_xslx.setVisible(false);
            t_csv.setVisible(false);
            current_tab_indicator.setLayoutX(385);
        } else if (event.getTarget() == btn_home)
        {
            t_home.setVisible(true);
            t_settings.setVisible(false);
            t_xslx.setVisible(false);
            t_csv.setVisible(false);
            current_tab_indicator.setLayoutX(16);
        } else if (event.getTarget() == btn_xslx)
        {
            t_xslx.setVisible(true);
            t_settings.setVisible(false);
            t_home.setVisible(false);
            t_csv.setVisible(false);
            current_tab_indicator.setLayoutX(185);
        } else if (event.getTarget() == btn_csv)
        {
            t_csv.setVisible(true);
            t_xslx.setVisible(false);
            t_settings.setVisible(false);
            t_home.setVisible(false);
            current_tab_indicator.setLayoutX(285);
        }
    }
    
    @FXML
    private void exit(MouseEvent event)
    {
           System.exit(0);
    }

    @FXML
    private void showAbout(MouseEvent event)
    {
        home_about_details.setVisible(true);
    }

    @FXML
    private void hideAbout(MouseEvent event)
    {
        home_about_details.setVisible(false);
    }

    @FXML
    private void xlsxBrowseFile(MouseEvent event)
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
            xlsx_filename.setText(selectedFile.getName());
            convertedFilename = selectedFile.getName();
            xlsx_convert_btn.setDisable(false);
            xlsx_progress_text.setVisible(false);
            xlsx_completed_text.setVisible(false);
            xlsx_checkmark.setVisible(false);
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
            settings_output_path.setText(selectedDirectory.getPath());
            outputDirectory = selectedDirectory.getPath();
        }
    }

    /**
     * For converting single xlsx file. *
     */
    @FXML
    private void xlsxConvertFile(MouseEvent event) throws IOException, InvalidFormatException, InterruptedException
    {
        System.out.println("Converting file. ");
        //m_progressbar.setDisable(false);
        xlsx_progress_text.setStyle("-fx-text-inner-color: white;");
        xlsx_progress_text.setVisible(true);

        ConvertXslxFile task = new ConvertXslxFile();
        new Thread(task).start();

        //Disable progressbar and display check mark when finished. 
        task.setOnSucceeded(e
                ->
        {
            //m_progressbar.setDisable(true);
            xlsx_completed_text.setStyle("-fx-text-inner-color: white;");
            xlsx_completed_text.setVisible(true);
            xlsx_checkmark.setVisible(true);
        });
    }

    //Remember to fix this and break it down to smaller functions.  
    class ConvertXslxFile extends Task<Void>
    {
        @Override
        protected Void call() throws Exception
        {
            try ( //Streams the file, so can't seek specific cells, but much faster than opening entire workbook. Workbook has hundreds of thousands of lines. 
                    InputStream is = new FileInputStream(selectedFile);
                    Workbook workbook = StreamingReader.builder()
                            .rowCacheSize(100)
                            .bufferSize(4096)
                            .open(is))
            {
                Sheet sheet = workbook.getSheetAt(workbook.getNumberOfSheets() - 1);
                String thisOutputDirectory = outputDirectory + "\\" +  convertedFilename.replace(".xlsx", "") + "-converted" + ".txt";
                PrintWriter tdfile = new PrintWriter(new FileWriter(thisOutputDirectory));

                LatLon latLon = UTMCoord.locationFromUTMCoord(10, AVKey.NORTH, 490599.86, 5458794.84);
                double latitude = latLon.getLatitude().degrees;
                double longitude = latLon.getLongitude().degrees;

                int counter = 0;
                String lat = "";
                String lon = "";
                for (Row r : sheet)
                {
                    for (int i = 0; i < 10; i++) //Need to use iterators to get null values between columns due to library limitations. 
                    {
                        Cell c = r.getCell(i);
                        if (c == null || "".equals(c.getStringCellValue()))
                        {
                            tdfile.print("N/A" + "\t");
                            counter++;
                        } 
                        
                        else //
                        {
                            if (counter % 10 < 8) //If not lat long, then just print value plus tab.  columns less than 8 are not coordinates. 
                            {
                                tdfile.print(c.getStringCellValue() + "\t");
                            } 
                            
                            else if (counter % 10 == 8) //If last digit is 8 or 9, then it is at columns for coordinates. Convert these.  
                            {
                                if ("0".equals(c.getStringCellValue())) //Sometimes value will be 0 to protect privacy. Can't use function to convert 0, it will  error. 
                                {
                                    tdfile.print("0" + "\t");
                                } 
                                else if ("X".equals(c.getStringCellValue())) //For catching the header values otherwise it will error when converted. 
                                {
                                    tdfile.print("X" + "\t");
                                } 
                                else //Anything else and it should be a valid coordinate value here for convertsion. 
                                {
                                    lat = c.getStringCellValue();
                                }
                            } 
                            
                            else if (counter % 10 == 9) //If last digit is 8 or 9, then it is lat and long, then convert. 
                            {
                                if ("0".equals(c.getStringCellValue()))
                                {
                                    tdfile.println("0");
                                } 
                                else if ("Y".equals(c.getStringCellValue())) //For catching the header values otherwise it will throw error when converted. 
                                {
                                    tdfile.println("Y");
                                } 
                                else //Anything else and it should be a valid coordinate value here for convertsion. 
                                {
                                    lon = c.getStringCellValue(); //Getting lat from instance variable. Need to enter both values for conversion function, but can't seek previous value in stream. 
                                    latLon = UTMCoord.locationFromUTMCoord(10, AVKey.NORTH, Double.parseDouble(lat), Double.parseDouble(lon));
                                    latitude = latLon.getLatitude().degrees;
                                    longitude = latLon.getLongitude().degrees;
                                    tdfile.println(latitude + "\t" + longitude);
                                }
                            } 
                            
                            else
                            {
                                System.out.println("Invalid value found. Counter: "+ counter + "Value: " + c.getStringCellValue());
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
                            xlsx_progress_text.setText("Rows Processed: " + countertext / 10);
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
        csv_drag_area.setText(fileList);
        csv_convert_btn.setDisable(false);
    }
    
    

    @FXML
    private void csvBulkConvert(MouseEvent event) throws IOException, InvalidFormatException, InterruptedException
    {
        System.out.println("Combining CSV files. ");
        csv_completed_text.setStyle("-fx-text-inner-color: white;");
        csv_completed_text.setText("Combining... ");
        csv_completed_text.setVisible(true);

        CombineCSV task = new CombineCSV();
        new Thread(task).start();

        //Disable progressbar and display check mark when finished. 
        task.setOnSucceeded(e
                ->
        {
            csv_completed_text.setText("Finished combining CSVs. ");
            csv_checkmark.setVisible(true);
        });
    }
    
class CombineCSV extends Task<Void>
{ 
    @Override
    protected Void call() throws FileNotFoundException, IOException
    {
        BufferedReader br = null;
        String line = "";
        String firstLine = "";
        boolean isFirstLine = true;
        
        String combinedFilename = bulkCSVFiles.get(0).getName();
        String thisOutputDirectory = outputDirectory + "\\" + combinedFilename.replace(".csv", "") + "-combined" + ".csv";
        PrintWriter combinedFile = new PrintWriter(new FileWriter(thisOutputDirectory));
        System.out.println(outputDirectory);
        for (int i = 0; i < bulkCSVFiles.size(); i++)
        {
            try
            {
                br = new BufferedReader(new FileReader(bulkCSVFiles.get(i)));
                while ((line = br.readLine()) != null)
                {
                    if (isFirstLine)
                    {
                        firstLine = line;
                        combinedFile.println(line);
                    }
                    
                    //Files received usually include this line for some reason. Cant get access to their code so will use this to check for it. 
                    else if (!line.equals(firstLine) && !line.equals(",,,,,,,,,,,,"))
                    {
                        combinedFile.println(line);
                    }
                    
                    /** Use this later for correcting the expected csv bugs. **/
                    //String[] cell = line.split(cvsSplitBy);
                    //System.out.println("Country [code= " + cell[0] + " , name=" + cell[0] + "]");
                    isFirstLine = false;
                }

            } finally
            {
                if (br != null)
                {
                    try
                    {
                        br.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        combinedFile.close();
        return null;
    }
}

    @FXML
    void csvDragEntered(DragEvent event)
    {
        //System.out.println("Entered");
        //c_dragArea.setText("Drop");
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

            if (path.endsWith(".csv"))
            {
                event
                        .acceptTransferModes(
                                TransferMode.ANY);
            }
        }
    }
    
    @FXML
    public void visitHyperlink(ActionEvent event) throws URISyntaxException, IOException
    {
        Desktop.getDesktop().browse(new URI("https://github.com/mt9304/cpctools"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        xlsx_convert_btn.setDisable(true);
        csv_convert_btn.setDisable(true);
        home_about_details.setStyle("-fx-text-inner-color: white;");
    }

}
