package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "spid",
})

public class ConsultaWORequest {
    @NotNull
    private Long spid;

    public Long getSpid(){
        return spid;
    }
    public void setSpid(Long spid ){ this.spid = spid; }

}
