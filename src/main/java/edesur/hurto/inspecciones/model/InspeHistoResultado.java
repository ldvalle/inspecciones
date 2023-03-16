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
        "numeroCliente",
        "tipoInspeccion",
        "codMotivo",
        "nroSolicitud",
        "area",
        "codEstado",
        "descripEstado",
        "idCaso",
        "fechaCreacion",
        "procesado",
        "idInspeccion"
})


public class InspeHistoResultado {

    private long    numeroCliente;
    private int     tipoInspeccion;
    private String  codMotivo;
    private String  nroSolicitud; // El IdWorkOrderActivity
    private String  area;
    private String  codEstado;
    private String  descripEstado;
    private String  idCaso;
    private Date    fechaCreacion;
    private Boolean procesado;
    private String  idInspeccion; // El nro.de solicitud

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public int getTipoInspeccion() {
        return tipoInspeccion;
    }

    public void setTipoInspeccion(int tipoInspeccion) {
        this.tipoInspeccion = tipoInspeccion;
    }

    public String getCodMotivo() {
        return codMotivo;
    }

    public void setCodMotivo(String codMotivo) {
        this.codMotivo = codMotivo;
    }

    public String getNroSolicitud() {
        return nroSolicitud;
    }

    public void setNroSolicitud(String nroSolicitud) {
        this.nroSolicitud = nroSolicitud;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getDescripEstado() {
        return descripEstado;
    }

    public void setDescripEstado(String descripEstado) {
        this.descripEstado = descripEstado;
    }

    public String getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(String idCaso) {
        this.idCaso = idCaso;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("fechaCreacion")
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
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
        sb.append(", numeroCliente=").append(numeroCliente);
        sb.append(", tipoInspeccion='").append(tipoInspeccion).append('\'');
        sb.append(", codMotivo='").append(codMotivo).append('\'');
        sb.append(", nroSolicitud=").append(nroSolicitud);
        sb.append(", codEstado='").append(codEstado).append('\'');
        sb.append(", descripEstado='").append(descripEstado).append('\'');
        sb.append(", idCaso='").append(idCaso).append('\'');
        sb.append(", fechaCreacion=").append(df.format(fechaCreacion));
        sb.append(", procesado=").append(procesado);
        sb.append(", idInspeccion=").append(idInspeccion);
        sb.append('}');
        return sb.toString();
    }

    private static final SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
}
