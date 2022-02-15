package edesur.hurto.inspecciones.model;

import edesur.hurto.inspecciones.model.InspeHistoResultado;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

public class InspeHistoResponse {

    private List<InspeHistoResultado> listaResultado = new ArrayList<>();

    public List<InspeHistoResultado> getListaResultado(){
        return listaResultado;
    }

    public void setListaResultado(List<InspeHistoResultado> listaResultado){
        this.listaResultado = listaResultado;
    }

}
