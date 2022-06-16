package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "idCaso",
        "numeroCliente",
        "codMotivo",
        "codCategoria",
        "codSubCategoria"
})

public class InspeSolicitudRequest {
    @NotNull
    private Long idCaso;

    @NotNull
    private Long numeroCliente;

    @NotNull
    private String codMotivo;

    @NotNull
    private String codCategoria;

    @NotNull
    private String codSubCategoria;

    // Setters & Getters
    public Long getIdCaso(){
        return idCaso;
    }
    public void setIdCaso(Long idCaso ){ this.idCaso = idCaso; }

    public Long getNumeroCliente(){
        return numeroCliente;
    }
    public void setNumeroCliente(Long numeroCliente ){ this.numeroCliente = numeroCliente; }

    public String getCodMotivo(){
        return codMotivo;
    }
    public void setCodMotivo(String codMotivo ){ this.codMotivo = codMotivo; }

    public String getCodCategoria() { return codCategoria; }
    public void setCodCategoria(String codCategoria) { this.codCategoria = codCategoria; }

    public String getCodSubCategoria() { return codSubCategoria; }
    public void setCodSubCategoria(String codSubCategoria) { this.codSubCategoria = codSubCategoria; }

}
