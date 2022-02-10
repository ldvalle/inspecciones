package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "nroSolicitud",
        "numeroCliente"
})


public class InspeStatusRequest2 {

    @NotNull
    private Long nroSolicitud;

    @NotNull
    private Long numeroCliente;

    // Setters & Getters
    public Long getNroSolicitud(){
        return nroSolicitud;
    }
    public void setNroSolicitud(Long nroSolicitud ){ this.nroSolicitud = nroSolicitud; }

    public Long getNumeroCliente(){
        return numeroCliente;
    }
    public void setNumeroCliente(Long numeroCliente ){ this.numeroCliente = numeroCliente; }


}
