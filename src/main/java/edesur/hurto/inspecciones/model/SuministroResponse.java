package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuministroResponse extends ResponseBase {
    @Size(min = 0)
    @Valid
    private List<SuministroResultado> listaResultado = new ArrayList<>();

    public List<SuministroResultado> getListaResultado() {
        return listaResultado;
    }

    public void setListaResultado(List<SuministroResultado> listaResultado) {
        this.listaResultado = listaResultado;
    }
}
