package edesur.hurto.inspecciones.model;

import edesur.hurto.inspecciones.model.ConsultaWOResponse;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

public class ConsultaWOResponse {

    private List<ConsultaWOResultado> listaResultado = new ArrayList<>();

    public List<ConsultaWOResultado> getListaResultado(){
        return listaResultado;
    }

    public void setListaResultado(List<ConsultaWOResultado> listaResultado){
        this.listaResultado = listaResultado;
    }
}
