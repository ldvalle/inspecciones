package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBase {
    @Size(max = 10)
    @NotNull
    private String codigo_retorno = "OK";

    @Size(max = 100)
    @NotNull
    private String descripcion_retorno = "Proceso Exitoso";

    public String getCodigo_retorno() {
        return codigo_retorno;
    }

    public void setCodigo_retorno(String codigo_retorno) {
        this.codigo_retorno = codigo_retorno;
    }

    public String getDescripcion_retorno() {
        return descripcion_retorno;
    }

    public void setDescripcion_retorno(String descripcion_retorno) {
        this.descripcion_retorno = descripcion_retorno;
    }
}
