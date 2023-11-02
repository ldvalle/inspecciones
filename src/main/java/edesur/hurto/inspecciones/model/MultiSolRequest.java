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
    @JsonProperty("SPID")
    private String SPID;

    @NotNull
    @JsonProperty("TypeOfSelection")
    private String TypeOfSelection;

    @NotNull
    @JsonProperty("IdOpportunity")
    private String IdOpportunity;

    //Setters & Getters
    public String getSPID() {return SPID; }
    public void setSPID(String SPID) { this.SPID = SPID; }

    public String getTypeOfSelection() {return TypeOfSelection; }
    public void setTypeOfSelection(String TypeOfSelection) { this.TypeOfSelection = TypeOfSelection; }

    public String getIdOpportunity() {return IdOpportunity; }
    public void setIdOpportunity(String IdOpportunity) { this.IdOpportunity = IdOpportunity; }

}
