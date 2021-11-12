package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edesur.hurto.inspecciones.Configuracion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "codigoEmpresa",
        "numeroSuministro",
        "fechaCorte",
        "fechaNotificacion",
        "pagoEnProceso",
        "fechaIngresoPago",
        "estadoConexion",
        "estadoSuministro",
        "periodoFacturacion",
        "toi",
        "cortePorDeuda",
        "estadoCobrabilidad",
        "fechaProximaFactura"
})
public class SuministroResultado {
    @Size(max = 999999999)
    @NotNull
    private int numeroSuministro;

    @Size(max = 4)
    @NotNull
    private String codigoEmpresa = Configuracion.id_empresa;

    @NotNull
    private Date fechaCorte;

    private Date fechaNotificacion;

    @Size(max = 1)
    @NotNull
    private String pagoEnProceso;

    private Date fechaIngresoPago;

    @Size(max = 1)
    @NotNull
    private String estadoConexion;

    @Size(max = 2)
    @NotNull
    private String estadoSuministro;

    @Size(max = 1)
    @NotNull
    private String periodoFacturacion;

    @Size(max = 1)
    @NotNull
    private String toi;

    @Size(max = 1)
    @NotNull
    private String cortePorDeuda;

    @Size(max = 20)
    @NotNull
    private String estadoCobrabilidad;

    private Date fechaProximaFactura;

    public int getNumeroSuministro() {
        return numeroSuministro;
    }

    public void setNumeroSuministro(int numeroSuministro) {
        this.numeroSuministro = numeroSuministro;
    }

    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Argentina/Buenos_Aires")
    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Argentina/Buenos_Aires")
    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public String getPagoEnProceso() {
        return pagoEnProceso;
    }

    public void setPagoEnProceso(String pagoEnProceso) {
        this.pagoEnProceso = pagoEnProceso;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Argentina/Buenos_Aires")
    public Date getFechaIngresoPago() {
        return fechaIngresoPago;
    }

    public void setFechaIngresoPago(Date fechaIngresoPago) {
        this.fechaIngresoPago = fechaIngresoPago;
    }

    public String getEstadoConexion() {
        return estadoConexion;
    }

    public void setEstadoConexion(String estadoConexion) {
        this.estadoConexion = estadoConexion;
    }

    public String getEstadoSuministro() {
        return estadoSuministro;
    }

    public void setEstadoSuministro(String estadoSuministro) {
        this.estadoSuministro = estadoSuministro;
    }

    public String getPeriodoFacturacion() {
        return periodoFacturacion;
    }

    public void setPeriodoFacturacion(String periodoFacturacion) {
        this.periodoFacturacion = periodoFacturacion;
    }

    public String getToi() {
        return toi;
    }

    public void setToi(String toi) {
        this.toi = toi;
    }

    public String getCortePorDeuda() {
        return cortePorDeuda;
    }

    public void setCortePorDeuda(String cortePorDeuda) {
        this.cortePorDeuda = cortePorDeuda;
    }

    public String getEstadoCobrabilidad() {
        return estadoCobrabilidad;
    }

    public void setEstadoCobrabilidad(String estadoCobrabilidad) {
        this.estadoCobrabilidad = estadoCobrabilidad;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Argentina/Buenos_Aires")
    public Date getFechaProximaFactura() {
        return fechaProximaFactura;
    }

    public void setFechaProximaFactura(Date fechaProximaFactura) {
        this.fechaProximaFactura = fechaProximaFactura;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SuministroResultado{");
        sb.append("numeroSuministro=").append(numeroSuministro);
        sb.append(", fechaCorte=").append(df.format(fechaCorte));
        sb.append(", pagoEnProceso.sql='").append(pagoEnProceso).append('\'');
        sb.append(", periodoFacturacion='").append(periodoFacturacion).append('\'');
        sb.append(", estadoCobrabilidad='").append(estadoCobrabilidad).append('\'');
        sb.append('}');
        return sb.toString();
    }

    private static final SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
}
