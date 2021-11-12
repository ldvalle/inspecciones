package edesur.hurto.inspecciones.model;

public class DataClienteResponse {

    private String   stsCliente;
    private String   stsFacturacion;
	 private String	tipoFpago;
	 private String	tipoReparto;
	 private String	esElectro;


    public String getEstadoCliente() { return  this.stsCliente; }
    public void setEstadoCliente( String stsCliente ) {this.stsCliente = stsCliente; }

    public String getEstadoFacturacion() { return  this.stsFacturacion; }
    public void setEstadoFacturacion( String stsFacturacion ) {this.stsFacturacion = stsFacturacion; }

    public String getTipoFpago() { return  this.tipoFpago; }
    public void setTipoFpago( String tipoFpago ) {this.tipoFpago = tipoFpago; }

    public String getTipoReparto() { return  this.tipoReparto; }
    public void setTipoReparto( String tipoReparto ) {this.tipoReparto = tipoReparto; }

    public String getEsElectro() { return  this.esElectro; }
    public void setEsElectro( String esElectro ) {this.esElectro = esElectro; }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataClienteResponse{");
        sb.append("stsCliente='").append(stsCliente).append('\'');
        sb.append(", stsFacturacion='").append(stsFacturacion).append('\'');
        sb.append(", tipoFpago='").append(tipoFpago).append('\'');
        sb.append(", tipoReparto='").append(tipoReparto).append('\'');
        sb.append(", esElectro='").append(esElectro).append('\'');
        return sb.toString();
    }
}
