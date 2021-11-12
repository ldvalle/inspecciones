package edesur.hurto.inspecciones.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import edesur.hurto.inspecciones.model.CorteRepoResultado;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsultaCorteRepoResponse extends ResponseBase{ 

	@NotNull
	private String codigoEmpresa;
	@NotNull
	private String numeroSuministro;

	private List<CorteRepoResultado> listaResultado = new ArrayList<>();

	public List<CorteRepoResultado> getListaResultado() {
	  return listaResultado;
	}
	public void setListaResultado(List<CorteRepoResultado> listaResultado) {
	  this.listaResultado = listaResultado;
	}

	public void setCodigoEmpresa(String codigoEmpresa){ this.codigoEmpresa = codigoEmpresa; }
	public String getCodigoEmpresa(){ return this.codigoEmpresa; }
	
	public void setNumeroSuministro(long numeroSuministro){
			String sNroSuministro = numeroSuministro + "AR";
  		   this.numeroSuministro = sNroSuministro;
	}

	public String getNumeroSuministro(){ return this.numeroSuministro; }
}
