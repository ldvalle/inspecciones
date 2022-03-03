package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.ConsultaWOResultado;
import edesur.hurto.inspecciones.model.ConsultaWOResponse;

import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.xml.ws.spi.ServiceDelegate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.Date;

public class ConsultaOrdenesTrabajo {
    public DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ConsultaWOResponse getOrdenes(long nroCliente) {
        ConsultaWOResponse regData = new ConsultaWOResponse();
        List<ConsultaWOResultado> regLista=null;
        try {
            regLista = getLista(nroCliente);
            regData.setListaResultado(regLista);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return regData;
    }

    private List<ConsultaWOResultado> getLista(long nroCliente){
        Vector<ConsultaWOResultado> miLista = new Vector<ConsultaWOResultado>();
        String SQL_QUERY ="";

        try {
            Connection conn = dataSource.getConnection();

            PreparedStatement stmt = null;

            //Creamos la tabla temporal
            stmt = conn.prepareStatement(CREATE_TEMP);
            stmt.executeUpdate();

            if(nroCliente > 80000000){
                long auxNroCliente=0;

                if(nroCliente > 80000000 && nroCliente < 80500000) {
                    auxNroCliente = nroCliente - 80000000;
                }else{
                    auxNroCliente = nroCliente;
                }

                stmt=null;
                stmt = conn.prepareStatement(INS_INSPE_T23);
                //stmt.setLong(1, nroCliente);
                stmt.setLong(1, auxNroCliente);
                stmt.executeUpdate();
            }else{
                stmt=null;
                stmt = conn.prepareStatement(INS_INSPE_T1);
                stmt.setLong(1, nroCliente);
                stmt.executeUpdate();

                stmt=null;
                stmt = conn.prepareStatement(INS_CAMMED);
                stmt.setLong(1, nroCliente);
                stmt.executeUpdate();

                stmt=null;
                stmt = conn.prepareStatement(INS_CAMTIT);
                stmt.setLong(1, nroCliente);
                stmt.executeUpdate();

            }

            try(PreparedStatement stmt2 = conn.prepareStatement(SEL_CONSULTA)) {
                try(ResultSet rs = stmt2.executeQuery()) {

                    while(rs.next()){
                        ConsultaWOResultado reg1 = new ConsultaWOResultado();

                        reg1.setSpid(rs.getLong(1));
                        reg1.setEventDate(rs.getDate(2));
                        reg1.setEvent(rs.getString(3));
                        reg1.setValue(rs.getInt(4));

                        miLista.add(reg1);
                    }
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return miLista;
    }

    private static final String CREATE_TEMP = "CREATE TEMP TABLE tempo1 ( " +
            "numero_cliente integer, " +
            "fecha_evento date, " +
            "tipo_evento char(10), " +
            "valor_evento int " +
            ") WITH NO LOG ";

    private static final String INS_INSPE_T1 = "INSERT INTO tempo1 (numero_cliente, fecha_evento, tipo_evento, valor_evento) " +
            "SELECT s.numero_cliente, DATE(i.fecha_inspeccion), 'Insp', " +
            "CASE " +
            "    WHEN i.tiene_anom_comerci IN (0, 3, 4, 6) AND s.estado = 7 THEN -2 " +
            "    ELSE 0 " +
            "END " +
            "FROM inspecc:in_solicitud s, inspecc:in_inspeccion i " +
            "WHERE s.numero_cliente = ? " +
            "AND s.estado IN (6, 7) " +
            "AND i.nro_solicitud = s.nro_solicitud ";

    private static final String INS_INSPE_T23 = "INSERT INTO tempo1 (numero_cliente, fecha_evento, tipo_evento, valor_evento) " +
            "SELECT s.numero_cliente, DATE(i.fecha_inspeccion), 'Insp', " +
            "CASE " +
            "    WHEN i.tiene_anom_comerci IN (0, 3, 4, 6) AND s.estado = 7 THEN -2 " +
            "    ELSE 0 " +
            "END " +
            "FROM inspect23:i3_solicitud s, inspect23:i3_inspeccion i " +
            "WHERE s.numero_cliente = ? " +
            "AND s.estado IN (6, 7) " +
            "AND i.nro_solicitud = s.nro_solicitud ";

    private static final String INS_CAMMED = "INSERT INTO tempo1 (numero_cliente, fecha_evento, tipo_evento, valor_evento) " +
            "SELECT DISTINCT numero_cliente, fecha_lectura, 'MetCh', 1 " +
            "FROM hislec " +
            "WHERE numero_cliente = ? " +
            "AND tipo_lectura in (5,6) ";

    private static final String INS_CAMTIT = "INSERT INTO tempo1 (numero_cliente, fecha_evento, tipo_evento, valor_evento) " +
            "SELECT s.numero_cliente, NVL(s.rst_fecha_inicio, date(e.fecha_finalizacion)), 'CustCH', 1 " +
            "FROM solicitud s, est_sol e " +
            "WHERE s.numero_cliente = ? " +
            "AND s.cam_tit = 'S' " +
            "AND e.nro_solicitud = s.nro_solicitud " +
            "AND e.fecha_finalizacion IS NOT NULL ";


    private static final String SEL_CONSULTA = "SELECT numero_cliente, fecha_evento, trim(tipo_evento), valor_evento " +
            "FROM tempo1 " +
            "ORDER BY fecha_evento DESC ";

}
