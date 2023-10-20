package edesur.hurto.inspecciones.model;

import edesur.hurto.inspecciones.model.InspeSolicitudRequest;
import java.util.ArrayList;
import java.util.List;

public class InspeMultiSolRequest {
    private List<InspeSolicitudRequest> listaSol = new ArrayList<>();

    public List<InspeSolicitudRequest> getListaSol(){
        return listaSol;
    }

    public void setListaSol(List<InspeSolicitudRequest> listaSol){
        this.listaSol=listaSol;
    }

}
