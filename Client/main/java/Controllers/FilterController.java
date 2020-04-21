package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.Festival.common.ConcertDTO;
import org.Festival.common.THRIFTService;
import org.apache.thrift.TException;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FilterController {
    @FXML
    private TableView<ConcertDTO> filterTable;
    @FXML
    private TableColumn<ConcertDTO, String> artistCol;
    @FXML
    private TableColumn<ConcertDTO, LocalDateTime> dateCol;
    @FXML
    private TableColumn<ConcertDTO, String> locCol;
    @FXML
    private TableColumn<ConcertDTO, Integer> freeSeatsCol;

    private THRIFTService.Client server;

    private LocalDate dateFilter;
    private ObservableList<ConcertDTO> model ;

    public FilterController(){ }

    @FXML
    public void initialize(){
        artistCol.setCellValueFactory(new PropertyValueFactory<ConcertDTO, String>("artist")); //
        dateCol.setCellValueFactory(new PropertyValueFactory<ConcertDTO, LocalDateTime>("date")); //
        locCol.setCellValueFactory(new PropertyValueFactory<ConcertDTO, String>("location")); //
        freeSeatsCol.setCellValueFactory(new PropertyValueFactory<ConcertDTO, Integer>("seatsFree")); //

    }

    public void setServer(THRIFTService.Client server) throws RemoteException {
        this.server = server;
        initModel();
    }


    private void initModel() throws RemoteException {
        model = FXCollections.observableArrayList();
        List<ConcertDTO> list = null;
        try {
            list = server.getConcertsByDate(dateFilter.toString());
            model.setAll(list);
            filterTable.setItems(model);
        } catch (TException e) {
            e.printStackTrace();
        }

    }


    public void initData(LocalDate date){
        this.dateFilter = date;
    }
}

