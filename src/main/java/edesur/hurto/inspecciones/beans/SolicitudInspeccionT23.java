package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeSolicitudResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SolicitudInspeccionT23 {

    public DataSource dataSourceSynergia;
    public DataSource dataSourceCandela;

    public void setDataSourceSynergia(DataSource dataSourceSynergia) {
        this.dataSourceSynergia = dataSourceSynergia;
    }

    public void setDataSourceCandela(DataSource dataSourceCandela) {
        this.dataSourceCandela = dataSourceCandela;
    }

    public InspeSolicitudResponse CreateSolicitud(long idCaso, long nroCliente, String sCodMotivo) {
        int iEstadoCliente = -1;
        int iTarifaCliente = -1;
        boolean MotivoValido = false;

        InspeSolicitudResponse regRes = new InspeSolicitudResponse();

        //TODO

        return regRes;
    }
}
