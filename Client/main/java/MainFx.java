import Controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.Festival.common.THRIFTService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class MainFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            TTransport transp = new TSocket("localhost", 9095);
            transp.open();
            TProtocol proto = new TBinaryProtocol(transp);

            THRIFTService.Client server = new THRIFTService.Client(proto);

            primaryStage.setTitle("Welcome to SpringFestival -- Login");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/LoginMPP.fxml"));
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setServer(server);
            Scene sc = new Scene(root);
            primaryStage.setScene(sc);
            primaryStage.show();
        }
        catch (Exception E){
            System.out.println("Nu-i deschis serverul");
            System.out.println(E.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
