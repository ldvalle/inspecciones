package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import edesur.hurto.inspecciones.validacion.CheckEmpresa;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultaCorteRepoRequest {
    @Size(min = 1, max = 1)
    @Valid
    @NotNull
    private List<Suministro> listaSuministros = new ArrayList<>();

    public List<Suministro> getListaResultado() {
        return listaSuministros;
    }

    public void setListaSuministros(List<Suministro> listaSuministros) {
        this.listaSuministros = listaSuministros;
    }

	/* NIVEL 3 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Suministro {
		
		@CheckEmpresa
		private String codigoEmpresa;

		@Size(max = 10)
		private String numeroSuministro;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "America/Argentina/Buenos_Aires")
		private Date fechaDesde;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd", timezone = "America/Argentina/Buenos_Aires")
		private Date fechaHasta;

		/*	Setter and Getters Nivel 3 */
		public String getCodigoEmpresa() { return codigoEmpresa; }
		public void setCodigoEmpresa(String codigoEmpresa) { this.codigoEmpresa = codigoEmpresa; }

		public void setNumeroSuministro(String numeroSuministro) { this.numeroSuministro = numeroSuministro; }
		public String getNumeroSuministro() { return numeroSuministro; }
		
		public void setFechaDesde(Date fechaDesde) { this.fechaDesde = fechaDesde; }
		public Date getFechaDesde() { return fechaDesde; }
		
		public void setFechaHasta(Date fechaHasta) { this.fechaHasta = fechaHasta; }
		public Date getFechaHasta() { return fechaHasta; }
		
	} 
}
