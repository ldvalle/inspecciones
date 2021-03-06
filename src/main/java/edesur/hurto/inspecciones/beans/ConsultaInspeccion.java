package edesur.hurto.inspecciones.beans;


import edesur.hurto.inspecciones.model.InspeStatusResponse;

import javax.sql.DataSource;
import javax.xml.ws.spi.ServiceDelegate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultaInspeccion {

    public DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public InspeStatusResponse getConsultaInspe(long idCaso, long nroCliente){
        InspeStatusResponse regData=null;

        try {
            Connection connection = dataSource.getConnection();
            //Buscar el caso
            regData = getSolicitud(idCaso, nroCliente, connection);

            //determinar si es respuesta automática
            if(regData.getIdCaso()<=0){
                regData.setIdCaso(idCaso);
                regData.setCodEstado2(0);
                regData.setCodEstado("KO");
                regData.setDescripEstado("Caso Inexistente");

                return regData;
            }

            if(regData.getNroSolicitud()<=0){
                return regData;
            }

            // Recuperar el estado de la inspeccion ya registrada
            regData = getEstSol(regData, connection);

        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return regData;
    }

    public InspeStatusResponse getConsultaInspecCliente(long nroSolicitud, long nroCliente){
        InspeStatusResponse regData=null;

        try {
            Connection connection = dataSource.getConnection();
            //Buscar el caso
            regData = getSolicitudCliente(nroSolicitud, nroCliente, connection);

            //determinar si es respuesta automática
            if(regData.getIdCaso()<=0){
                regData.setNroSolicitud(nroSolicitud);
                regData.setCodEstado2(0);
                regData.setCodEstado("KO");
                regData.setDescripEstado("Caso Inexistente");

                return regData;
            }

            if(regData.getNroSolicitud()<=0){
                return regData;
            }

            // Recuperar el estado de la inspeccion ya registrada
            regData = getEstSol(regData, connection);

        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return regData;
    }

    private InspeStatusResponse getSolicitud(long idCaso, long nroCliente, Connection conn)throws SQLException{
        InspeStatusResponse reg = new InspeStatusResponse();

        reg.setIdCaso(-1);

        try(PreparedStatement stmt = conn.prepareStatement(SEL_SOL)) {
            stmt.setLong(1, idCaso);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    reg.setIdCaso(rs.getLong(1));
                    reg.setNumeroCliente(rs.getLong(2));
                    reg.setNroSolicitud(rs.getLong(3));
                    reg.setTarifa(rs.getInt(4));
                    reg.setCodEstado2(rs.getInt(5));
                    reg.setDescripEstado(rs.getString(6));
                    reg.setFechaEstado(rs.getDate(7));

                    if(reg.getCodEstado2()<1 || reg.getCodEstado2() > 10 || reg.getCodEstado2()==3){
                        reg.setCodEstado("KO");
                        reg.setsProcesado("N");
                    }else{
                        reg.setCodEstado("OK");
                        reg.setsProcesado("S");
                    }
                }
            }
        }

        return reg;
    }

    private InspeStatusResponse getSolicitudCliente(long nroSolicitud, long nroCliente, Connection conn) throws SQLException{
        InspeStatusResponse reg = new InspeStatusResponse();

        reg.setIdCaso(-1);

        try(PreparedStatement stmt = conn.prepareStatement(SEL_XSOLICITUD)) {
            stmt.setLong(1, nroSolicitud);
            stmt.setLong(2, nroCliente);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    reg.setIdCaso(rs.getLong(1));
                    reg.setNumeroCliente(rs.getLong(2));
                    reg.setNroSolicitud(rs.getLong(3));
                    reg.setTarifa(rs.getInt(4));
                    reg.setCodEstado2(rs.getInt(5));
                    reg.setDescripEstado(rs.getString(6));
                    reg.setFechaEstado(rs.getDate(7));

                    if(reg.getCodEstado2()<1 || reg.getCodEstado2() > 10 || reg.getCodEstado2()==3){
                        reg.setCodEstado("KO");
                        reg.setsProcesado("N");
                    }else{
                        reg.setCodEstado("OK");
                        reg.setsProcesado("S");
                    }
                }
            }
        }

        return reg;
    }

    private InspeStatusResponse getEstSol(InspeStatusResponse regBase, Connection conn)throws SQLException{
        InspeStatusResponse regFin=new InspeStatusResponse();
        String SQL="";

        regFin.setIdCaso(regBase.getIdCaso());
        regFin.setNroSolicitud(regBase.getNroSolicitud());
        regFin.setNumeroCliente(regBase.getNumeroCliente());

        regFin.setTarifa(regBase.getTarifa());

        if(regFin.getTarifa()==1){
            SQL= SEL_ESTADO_T1;
        }else{
            SQL= SEL_ESTADO_T23;
        }

        try(PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setLong(1, regFin.getNroSolicitud());
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    regFin.setCodEstado2(rs.getInt(1));
                    regFin.setDescripEstado(rs.getString(2));
                    regFin.setFechaEstado(rs.getDate(3));

                    if (regFin.getCodEstado2() < 1 || regFin.getCodEstado2() > 10 || regFin.getCodEstado2() == 3) {
                        regFin.setCodEstado("KO");
                        regFin.setsProcesado("N");
                    } else {
                        regFin.setCodEstado("OK");
                        regFin.setsProcesado("S");
                    }
                }
            }
        }

        return regFin;
    }


    private static final String SEL_SOL = "SELECT id_caso, numero_cliente, nro_solicitud_inspeccion, " +
            "tarifa, cod_estado, desc_estado, fecha_estado " +
            "FROM sol_inspecciones " +
            "WHERE id_caso = ? ";

    private static final String SEL_XSOLICITUD = "SELECT id_caso, numero_cliente, nro_solicitud_inspeccion, " +
            "tarifa, cod_estado, desc_estado, fecha_estado " +
            "FROM sol_inspecciones " +
            "WHERE nro_solicitud_inspeccion = ? " +
            "AND numero_cliente = ? ";


    private static final String SEL_ESTADO_T1 = "SELECT e.codigo_estado, TRIM(e.descripcion), s.fecha_estado " +
            "FROM inspecc:in_solicitud s, inspecc:in_estado_solic e " +
            "WHERE s.nro_solicitud = ? " +
            "AND e.codigo_estado = s.estado ";

    private static final String SEL_ESTADO_T23 = "SELECT e.codigo_estado, TRIM(e.descripcion), s.fecha_estado " +
            "FROM inspect23:i3_solicitud s, inspecc:in_estado_solic e " +
            "WHERE s.nro_solicitud = ? " +
            "AND e.codigo_estado = s.estado ";

}
