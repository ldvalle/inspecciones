package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pointCode",
        "typeOrder",
        "TypeOfSelection",
        "idWorkOrderActivity",
        "area",
        "statusResult",
        "resultDescription",
        "idOpportunity",
        "dtCreation",
        "flgProcessed",
        "idInspeccion"
})


public class InspeHistoResultado {

    private long    pointCode;
    private int     typeOrder;
    private String  typeOfSelection;
    private String  idWorkOrderActivity; // El IdWorkOrderActivity
    private String  area;
    private String  statusResult;
    private String  resultDescription;
    private String  idOpportunity;
    private Date    dtCreation;
    private Boolean flgProcessed;
    private String  idInspeccion; // El nro.de solicitud

    public long getPointCode() {
        return pointCode;
    }

    public void setPointCode(long pointCode) {
        this.pointCode = pointCode;
    }

    public int getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(int typeOrder) {
        this.typeOrder = typeOrder;
    }

    public String getTypeOfSelection() {
        return typeOfSelection;
    }

    public void setTypeOfSelection(String typeOfSelection) {
        this.typeOfSelection = typeOfSelection;
    }

    public String getIdWorkOrderActivity() {
        return idWorkOrderActivity;
    }

    public void setIdWorkOrderActivity(String idWorkOrderActivity) {
        this.idWorkOrderActivity = idWorkOrderActivity;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatusResult() {
        return statusResult;
    }

    public void setStatusResult(String statusResult) {
        this.statusResult = statusResult;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public String getIdOpportunity() {
        return idOpportunity;
    }

    public void setIdOpportunity(String idOpportunity) {
        this.idOpportunity = idOpportunity;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("dtCreation")
    public Date getDtCreation() {
        return dtCreation;
    }

    public void setDtCreation(Date dtCreation) {
        this.dtCreation = dtCreation;
    }

    public Boolean getFlgProcessed() {
        return flgProcessed;
    }

    public void setFlgProcessed(Boolean flgProcessed) {
        this.flgProcessed = flgProcessed;
    }

    public String getIdInspeccion() {
        return idInspeccion;
    }

    public void setIdInspeccion(String idInspeccion) {
        this.idInspeccion = idInspeccion;
    }

    /********/
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InspeHistoResponse{");
        sb.append(", pointCode=").append(pointCode);
        sb.append(", typeOrder='").append(typeOrder).append('\'');
        sb.append(", typeOfSelection='").append(typeOfSelection).append('\'');
        sb.append(", idWorkOrderActivity='").append(idWorkOrderActivity).append('\'');
        sb.append(", area='").append(area).append('\'');
        sb.append(", statusResult='").append(statusResult).append('\'');
        sb.append(", resultDescription='").append(resultDescription).append('\'');
        sb.append(", idOpportunity='").append(idOpportunity).append('\'');
        sb.append(", dtCreation=").append(df.format(dtCreation));
        sb.append(", flgProcessed=").append(flgProcessed);
        sb.append(", idInspeccion=").append(idInspeccion);
        sb.append('}');
        return sb.toString();
    }

    private static final SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
}
