package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "numeroCliente",
})

public class InspeHistoRequest {

    @NotNull
    private Long numeroCliente;

    public Long getNumeroCliente(){
        return numeroCliente;
    }
    public void setNumeroCliente(Long numeroCliente ){ this.numeroCliente = numeroCliente; }

}
