package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.*;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SPID",
        "TypeOfSelection",
        "IdOpportunity"
})

public class MultiSolRequest {
    @NotNull
    private String SPID;

    @NotNull
    private String TypeOfSelection;

    @NotNull
    private String IdOpportunity;

    //Setters & Getters
    public String getSPID() {return SPID; }
    public void setSPID(String SPID) { this.SPID = SPID; }

    public String getTypeOfSelection() {return TypeOfSelection; }
    public void setTypeOfSelection(String TypeOfSelection) { this.TypeOfSelection = TypeOfSelection; }

    public String getIdOpportunity() {return IdOpportunity; }
    public void setIdOpportunity(String IdOpportunity) { this.IdOpportunity = IdOpportunity; }

}
