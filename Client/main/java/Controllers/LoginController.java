package Controllers;
import Observer.Observer;
import Observer.UpdateServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.Festival.common.THRIFTService;
import org.Festival.common.UpdateService;
import org.Festival.common.User;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.rmi.RemoteException;

public class LoginController {
    @FXML
    public TextField user;
    public PasswordField passwd;
    public Button login;
    private THRIFTService.Client server;
    private int updatePort;

    public UpdateServer updateServer;

    @FXML
    public void initialize(){

    }

    public LoginController(){}

    public LoginController(THRIFTService.Client server){
        this.server = server;
    }

    public void setService(THRIFTService.Client srv){
        this.server = srv;
    }

    public void setServer(THRIFTService.Client server){
        this.updateServer = new UpdateServer();
        this.server = server;
    }

    private TServerTransport getServerSocket() {
        TServerTransport serverTransport;
        for(int i=1000;i<=55555;i++){
            try {
                serverTransport =  new TServerSocket(i);
                if(serverTransport != null){
                    this.updatePort = i;
                    return serverTransport;
                }

            } catch (TTransportException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean validateLogInController(String userName, String pass) throws RemoteException {
        try {
            return server.Login(new User(userName, pass), this.updatePort);
        } catch (TException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void onLogin(ActionEvent actionEvent) {
        String userName = user.getText();
        String pass = passwd.getText();

        Stage newStage = new Stage();
        newStage.setTitle("Willkomen, " + userName);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/mainApp.fxml"));
        AnchorPane mainPane = null;
        try {
            mainPane = (AnchorPane) loader.load();
            AppController controller;
            controller = loader.getController();
            TServerTransport serverTransport = getServerSocket();
            boolean logInSuccess = validateLogInController(userName, pass);
            if(!logInSuccess){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Autentificare nereusita");
                a.show();
                return;
            }
            //starting the update server
            UpdateServer updateServer = this.updateServer;
            UpdateService.Processor processor = new UpdateService.Processor<>(updateServer);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            Observer observer = new Observer(server);
            Thread thread = new Thread(observer);
            thread.start();
            System.out.println("Started update server on port " + this.updatePort);
            controller.setServer(this.server);
            updateServer.setController(controller);
            Scene newScene = new Scene(mainPane);
            newStage.setScene(newScene);
            login.getScene().getWindow().hide();
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
