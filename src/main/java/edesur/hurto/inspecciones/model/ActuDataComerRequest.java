package edesur.hurto.inspecciones.model;

//import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import edesur.hurto.inspecciones.validacion.CheckEmpresa;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
//import java.util.Date;
//import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)


public class ActuDataComerRequest {
    //@CheckEmpresa
    @Size(max = 4)
    private String codigoEmpresa;

    @NotNull
    @Size(max = 12)
    private String numeroSuministro;
    //private String numeroCliente;

    @Size(max = 15)
    private String numeroOrden;

    @NotNull
    @Size(max = 10)
    private String pod;

    @Size(max = 40)
    private String nombreCliente;

    @Size(max = 4)
    private String tipoDocumento;

	@Size(max = 20)
	private String nroDocumento;
	
    @Size(max = 30)
    private String telefonoCliente;

    @Size(max = 60)
    private String eMail;	
    
    //Los que no vamos a usar
    @Size(max = 2)
    private String	tipoCliente;
	 @Size(max = 10)
	 private String	calleDomicilio;
	 @Size(max = 5)
	 private String	numeroDomicilio;
	 @Size(max = 6)
	 private String	pisoDomicilio;
	 @Size(max = 6)
	 private String	dptoDomicilio;
	 @Size(max = 6)
	 private String	zipCodeDomicilio;	 
	 @Size(max = 1)
	 private String	tipoTitular;
	 @Size(max = 6)
	 private String	actividadEconomica;	 
	 
	 	 
    public String getCodigoEmpresa() {
        return codigoEmpresa;
    }
    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }
/*
    public String getNumeroSuministro() {
        return numeroSuministro;
    }
    public void setNumeroSuministro(String numeroSuministro) {
        this.numeroSuministro = numeroSuministro;
    }
*/
    public String getNumeroSuministro() {
        return numeroSuministro;
    }
    public void setNumeroSuministro(String numeroSuministro) {
        this.numeroSuministro = numeroSuministro;
    }

	public String getNumeroOrden(){ return numeroOrden; }
	public void setNumeroOrden(String numeroOrden) { this.numeroOrden = numeroOrden; }
	
	public String getPod(){ return pod; }
	public void setPod(String pod) { this.pod = pod; }
	
	public String getNombreCliente(){ return nombreCliente; }
	public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
	
	public String getTipoDocumento(){ return tipoDocumento; }
	public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
	
	public String getNroDocumento(){ return nroDocumento; }
	public void setNroDocumento(String nroDocumento) { this.nroDocumento = nroDocumento; }

	public String getTelefonoCliente(){ return telefonoCliente; }
	public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

	public String getEmail(){ return eMail; }
	public void setEmail(String eMail) { this.eMail = eMail; }	

	//Los que no vamos a usar
	public String getTipoCliente(){ return tipoCliente; }
	public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
	
	public String getCalleDomicilio(){ return calleDomicilio; }
	public void setCalleDomicilio(String calleDomicilio) { this.calleDomicilio = calleDomicilio; }	

	public String getNumeroDomicilio(){ return numeroDomicilio; }
	public void setNumeroDomicilio(String numeroDomicilio) { this.numeroDomicilio = numeroDomicilio; }	
	
	public String getPisoDomicilio(){ return pisoDomicilio; }
	public void setPisoDomicilio(String pisoDomicilio) { this.pisoDomicilio = pisoDomicilio; }	
	
	public String getDptoDomicilio(){ return dptoDomicilio; }
	public void setDptoDomicilio(String dptoDomicilio) { this.dptoDomicilio = dptoDomicilio; }	

	public String getZipCodeDomicilio(){ return zipCodeDomicilio; }
	public void setZipCodeDomicilio(String zipCodeDomicilio) { this.zipCodeDomicilio = zipCodeDomicilio; }
	
	public String getTipoTitular(){ return tipoTitular; }
	public void setTipoTitular(String tipoTitular) { this.tipoTitular = tipoTitular; }	

	public String getActividadEconomica(){ return actividadEconomica; }
	public void setActividadEconomica(String actividadEconomica) { this.actividadEconomica = actividadEconomica; }

	
}
