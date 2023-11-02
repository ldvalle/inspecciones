package edesur.hurto.inspecciones.model;

import edesur.hurto.inspecciones.model.MultiSolRequest;
import java.util.ArrayList;
import java.util.List;

public class ListaMultiSolRequest {
    private List<MultiSolRequest> listadoSol = new ArrayList<>();

    public List<MultiSolRequest> getListadoSol(){
        return listadoSol;
    }

    public void setListadoSol(List<MultiSolRequest> listaSol){
        this.listadoSol = listaSol;
    }
}
