package Controllers;

import Observer.IObserver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.Festival.common.Bilet;
import org.Festival.common.ConcertDTO;
import org.Festival.common.THRIFTService;
import org.apache.thrift.TException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppController implements IObserver {
    @FXML
    public Button LogOutBtn;
    public Button sellBtn;
    public TextField nrBileteTxt;
    public TextField numeTxt;
    public Button searchBtn;
    public DatePicker dataPicker;

    public TableView<ConcertDTO> tabel;
    public TableView<ConcertDTO> rezFin;

    public TableColumn<ConcertDTO, String> artistCol;
    public TableColumn<ConcertDTO, Integer> idCol;
    public TableColumn<ConcertDTO, String> locatieCol;
    public TableColumn<ConcertDTO, LocalDateTime> dataCol;
    public TableColumn<ConcertDTO, Integer> OcupCol;
    public TableColumn<ConcertDTO, Integer> FreeCol;

    public TableColumn<ConcertDTO, String> artistRez;
    public TableColumn<ConcertDTO, String> locatieRez;
    public TableColumn<ConcertDTO, LocalDateTime> dataRez;
    public TableColumn<ConcertDTO, Integer> freeRez;

    public ObservableList<ConcertDTO> concertModel;
    public ObservableList<ConcertDTO> resultModel;

    @FXML
    public void initialize(){
        rezFin.setVisible(false);
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("concertID"));
        locatieCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        OcupCol.setCellValueFactory(new PropertyValueFactory<>("seatsSold"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        FreeCol.setCellValueFactory(new PropertyValueFactory<>("seatsFree"));

        artistRez.setCellValueFactory(new PropertyValueFactory<>("artist"));
        locatieRez.setCellValueFactory(new PropertyValueFactory<>("location"));
        dataRez.setCellValueFactory(new PropertyValueFactory<>("date"));
        freeRez.setCellValueFactory(new PropertyValueFactory<>("seatsFree"));

    }

    public void initModel(){
        try{
            System.out.println("--- Main app retreiving concerts");
            concertModel = FXCollections.observableArrayList(server.getConcerts());
            for (ConcertDTO c: concertModel
                 ) {
                System.out.println(c);
            }
            tabel.getItems().setAll(concertModel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onLogout(ActionEvent actionEvent) {
        Node n = (Node) actionEvent.getSource();
        Stage st = (Stage) n.getScene().getWindow();
        st.close();
        return;
    }

    public void onSell(ActionEvent actionEvent) {
        ConcertDTO c = tabel.getSelectionModel().getSelectedItem();
        int nrTot = c.getSeatsFree();
        int nrBilete = 0;
        if (c == null){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("No concert selected");
            a.show();
        }else{
            try{
                nrBilete = Integer.parseInt(nrBileteTxt.getText());
            }
            catch (Exception e){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Nu ati introdus numarul de locuri");
                a.show();
            }
            if (nrBilete > nrTot){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setHeaderText("Locuri insuficiente");
                a.show();
            }
            else{
                String customer = numeTxt.getText();
                int concert = c.getConcertID();
                Bilet b = new Bilet(concert, customer, nrBilete);
                try{
                    server.SellBilet(b);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public THRIFTService.Client server;

    public void setServer(THRIFTService.Client server) {
        this.server = server;
        initModel();

    }


    @Override
    public void update() throws RemoteException {
        System.out.println("--- Main App update");
        Platform.runLater(this::initModel);
    }

    public void onSearch(ActionEvent actionEvent) {
        Stage newStage = new Stage();
        newStage.setTitle("Concerts");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/resultCtrl.fxml"));
        AnchorPane mainPane;
        try {
            mainPane = loader.load();
            FilterController controller = loader.getController();
            LocalDate filterDate = dataPicker.getValue();
            if(filterDate == null){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Nu ati selectat concert");
                a.show();
                return;
            }
            controller.initData(filterDate);
            controller.setServer(server);
            Scene newScene = new Scene(mainPane);
            newStage.setScene(newScene);
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setModel(List<ConcertDTO> lista) {
        concertModel = FXCollections.observableArrayList(lista);
        tabel.getItems().setAll(concertModel);
    }
}
