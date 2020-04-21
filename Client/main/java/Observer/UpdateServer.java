package Observer;
import Controllers.AppController;
import org.Festival.common.ConcertDTO;
import org.Festival.common.UpdateService;
import org.apache.thrift.TException;

import java.util.List;

public class UpdateServer implements UpdateService.Iface{
    AppController appCtrl = null;

    public UpdateServer(){

    }

    public void setController(AppController ctrl){
        appCtrl = ctrl;
    }


    @Override
    public void update(List<ConcertDTO> lista) throws TException {
        appCtrl.setModel(lista);
    }
}
