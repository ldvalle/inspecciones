package edesur.hurto.inspecciones.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "idCaso",
        "numeroCliente",
        "nroSolicitud",
        "tarifa",
        "codEstado",
        "descripEstado",
        "fechaEstado"
})

public class InspeStatusResponse {

    @Size(max = 999999999)
    @NotNull
    private long idCaso;

    @Size(max = 999999999)
    @NotNull
    private long numeroCliente;

    @Size(max = 999999999)
    private long nroSolicitud;

    @NotNull
    private int tarifa;

    @Size(max = 999)
    @NotNull
    private int codEstado;

    @NotNull
    private String descripEstado;

    private Date fechaEstado;

    /* setters & getters */

    public long getIdCaso() {
        return idCaso;
    }

    public void setIdCaso(long idCaso) {
        this.idCaso = idCaso;
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public long getNroSolicitud() {
        return nroSolicitud;
    }

    public void setNroSolicitud(long nroSolicitud) {
        this.nroSolicitud = nroSolicitud;
    }

    public int getTarifa() {
        return tarifa;
    }

    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }

    public int getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(int codEstado) {
        this.codEstado = codEstado;
    }

    public String getDescripEstado() {
        return descripEstado;
    }

    public void setDescripEstado(String descripEstado) {
        this.descripEstado = descripEstado;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("fechaEstado")
    public Date getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(Date fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    /********/
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InspeStatusResponse{");
        sb.append("idCaso=").append(idCaso);
        sb.append(", numeroCliente=").append(numeroCliente);
        sb.append(", nroSolicitud=").append(nroSolicitud);
        sb.append(", tarifa=").append(tarifa);
        sb.append(", codEstado=").append(codEstado);
        sb.append(", descripEstado='").append(descripEstado).append('\'');
        sb.append(", fechaEstado=").append(df.format(fechaEstado));
        sb.append('}');
        return sb.toString();
    }

    private static final SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
}
