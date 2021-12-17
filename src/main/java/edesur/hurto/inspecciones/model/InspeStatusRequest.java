package edesur.hurto.inspecciones.model;


import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "idCaso",
        "numeroCliente"
})

public class InspeStatusRequest {

    @NotNull
    private Long idCaso;

    @NotNull
    private Long numeroCliente;

    // Setters & Getters
    public Long getIdCaso(){
        return idCaso;
    }
    public void setIdCaso(Long idCaso ){ this.idCaso = idCaso; }

    public Long getNumeroCliente(){
        return numeroCliente;
    }
    public void setNumeroCliente(Long numeroCliente ){ this.numeroCliente = numeroCliente; }

}
