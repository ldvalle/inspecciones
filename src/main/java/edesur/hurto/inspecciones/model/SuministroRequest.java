package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import edesur.hurto.inspecciones.validacion.CheckEmpresa;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuministroRequest {
    @Size(min = 1, max = 1)
    @Valid
    @NotNull
    private List<Suministro> listaResultado = new ArrayList<>();

    public List<Suministro> getListaResultado() {
        return listaResultado;
    }

    public void setListaResultado(List<Suministro> listaResultado) {
        this.listaResultado = listaResultado;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Suministro {
        @DecimalMax("999999999")
        @NotNull
        private long numeroSuministro;

        @NotNull
        @CheckEmpresa
        private String codigoEmpresa;

        public long getNumeroSuministro() {
            return numeroSuministro;
        }

        public void setNumeroSuministro(long numeroSuministro) {
            this.numeroSuministro = numeroSuministro;
        }

        public String getCodigoEmpresa() {
            return codigoEmpresa;
        }

        public void setCodigoEmpresa(String codigoEmpresa) {
            this.codigoEmpresa = codigoEmpresa;
        }
    }
}
