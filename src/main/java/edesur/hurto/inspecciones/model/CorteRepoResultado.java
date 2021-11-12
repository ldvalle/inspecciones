package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"numeroSuministro",
   "nroOrdenSap",
   "idCorteRepo",
	"tipoRegistro",
	"motivo",
	"estado",
	"fechaEjecucion",
	"fechaSolicitud",
	"accionRealizada",
	"usuario",
	"tipo"
})

public class CorteRepoResultado{ 
	
   //@JsonIgnore
    private Date fechaEjecucion;
   
   @NotNull
	private long numeroSuministro;
	@NotNull
	private String nroOrdenSap;
	@NotNull
	private String idCorteRepo;
	@NotNull
	private String tipoRegistro;
	@NotNull
	private String	motivo;
	@NotNull
	private String estado;
	
	@NotNull
	private Date fechaSolicitud;
	@NotNull
	private String	accionRealizada;
	@NotNull
	private String usuario;
	@NotNull
	private String tipo;
	
	public long getNumeroSuministro() { return numeroSuministro;	}
	public void setNumeroSuministro(long numeroSuministro) { this.numeroSuministro = numeroSuministro; }	
	
	public void setNroOrdenSap(String nroOrdenSap){ this.nroOrdenSap = nroOrdenSap; }
	public String getNroOrdenSap(){ return nroOrdenSap; }
	
	public void setIdCorteRepo(String idCorteRepo){ this.idCorteRepo = idCorteRepo; }
	public String getIdCorteRepo(){ return idCorteRepo; }
	
	public void setTipoRegistro(String tipoRegistro){ this.tipoRegistro = tipoRegistro; }
	public String getTipoRegistro(){ return tipoRegistro; }

	public void setMotivo(String motivo){ this.motivo = motivo; }
	public String getMotivo(){ return motivo; }

	public void setEstado(String estado){ this.estado = estado; }
	public String getEstado(){ return estado; }

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Argentina/Buenos_Aires")
	public void setFechaEjecucion(Date fechaEjecucion){ this.fechaEjecucion = fechaEjecucion; }
	public Date getFechaEjecucion() { return fechaEjecucion; }

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Argentina/Buenos_Aires")
	public void setFechaSolicitud(Date fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
	public Date getFechaSolicitud() { return fechaSolicitud; }

	public void setAccionRealizada(String accionRealizada){ this.accionRealizada = accionRealizada; }
	public String getAccionRealizada(){ return accionRealizada; }
	
	public void setUsuario(String usuario){ this.usuario = usuario; }
	public String getUsuario(){ return usuario; }

	public void setTipo(String tipo){ this.tipo = tipo; }
	public String getTipo(){ return tipo; }

	@Override
	public String toString() {
	  final StringBuilder sb = new StringBuilder("CorteRepoResultado{");
	  sb.append(", 'numeroSuministro'=").append(numeroSuministro);
	  sb.append(", 'nroOrdenSap'='").append(nroOrdenSap).append('\'');
	  sb.append(", 'idCorteRepo'='").append(idCorteRepo).append('\'');
	  sb.append(", 'tipoRegistro'='").append(tipoRegistro).append('\'');
	  sb.append(", 'motivo'='").append(motivo).append('\'');
	  sb.append(", 'estado'='").append(estado).append('\'');
      sb.append(", 'fechaEjecucion'=").append(fechaEjecucion);
	  sb.append(", 'fechaSolicitud'=").append(fechaSolicitud);
	  sb.append(", 'accionRealizada'='").append(accionRealizada).append('\'');
	  sb.append(", 'usuario'='").append(usuario).append('\'');
	  sb.append(", 'tipo'='").append(tipo).append('\'');
	  
	  sb.append('}');
	  return sb.toString();
	}

}
