package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeHistoResultado;
import edesur.hurto.inspecciones.model.InspeHistoResponse;
import edesur.hurto.inspecciones.model.InspeConsulta;
import edesur.hurto.inspecciones.model.InspeStatusResponse;

import javax.sql.DataSource;
import javax.xml.ws.spi.ServiceDelegate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.Date;

public class ConsultaHistoInspecciones {
    public DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public InspeHistoResponse getHistoInspe(long nroCliente) {
        InspeHistoResponse regData = new InspeHistoResponse();
        List<InspeHistoResultado> regLista=null;
        try {
            regLista = getLista(nroCliente);
            regData.setListaResultado(regLista);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return regData;
    }

    private List<InspeHistoResultado> getLista(long nroCliente){
        Vector<InspeHistoResultado> miLista = new Vector<InspeHistoResultado>();
        String SQL_QUERY ="";

        if(nroCliente > 80000000){
            SQL_QUERY = SEL_CONSULTA_T23;
        }else{
            SQL_QUERY = SEL_CONSULTA_T1;
        }

        try {
            Connection conn = dataSource.getConnection();
            try(PreparedStatement stmt = conn.prepareStatement(SQL_QUERY)) {
                stmt.setLong(1, nroCliente);
                try(ResultSet rs = stmt.executeQuery()) {

                    while(rs.next()){
                        InspeConsulta reg1 = new InspeConsulta();
                        InspeHistoResultado reg2 = new InspeHistoResultado();

                        reg1.numero_cliente = rs.getLong(1);
                        reg1.tipo_extractor = rs.getInt(2);
                        reg1.cod_motivo = rs.getString(3);
                        reg1.sucursal = rs.getString(4);
                        reg1.estado_solicitud = rs.getInt(5);
                        reg1.descrip_estado_solicitud=rs.getString(6);
                        reg1.cod_estado_caso = rs.getInt(7);
                        reg1.desc_estado_caso = rs.getString(8);
                        reg1.id_caso = rs.getString(9);
                        reg1.fecha_caso = rs.getDate(10);
                        reg1.fecha_solicitud = rs.getDate(11);
                        reg1.nroSolicitud = rs.getString(12);
                        reg1.workOrderId = rs.getString(13);

                        //-- Traspaso
                        reg2.setNumeroCliente(reg1.numero_cliente);
                        reg2.setTipoInspeccion(reg1.tipo_extractor);
                        reg2.setCodMotivo(reg1.cod_motivo);
                        reg2.setNroSolicitud(reg1.workOrderId);
                        reg2.setArea(reg1.sucursal);
                        if(reg1.cod_estado_caso < 1 || reg1.cod_estado_caso > 10){
                            reg2.setCodEstado("KO");
                            reg2.setDescripEstado(reg1.desc_estado_caso);
                        }else{
                            reg2.setCodEstado("OK");
                            reg2.setDescripEstado(reg1.descrip_estado_solicitud);
                        }
                        reg2.setIdCaso(reg1.id_caso);
                        reg2.setFechaCreacion(reg1.fecha_caso);
                        if(reg1.estado_solicitud != 3 && reg1.estado_solicitud != 7 && reg1.estado_solicitud != 9){
                            reg2.setProcesado(false);
                        }else{
                            reg2.setProcesado(true);
                        }
                        reg2.setIdInspeccion(reg1.nroSolicitud);

                        miLista.add(reg2);
                    }
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return miLista;
    }

    private static final String SEL_CONSULTA_T1 = "SELECT DISTINCT t.numero_cliente, s.tipo_extractor, t.type_of_selection," +
            "s.sucursal, " +
            "s.estado, t1.descripcion, " +
            "t.cod_estado, t.desc_estado, " +
            "t.id_caso, t.fecha_estado, s.fecha_solicitud, " +
            "1 || LPAD(s.nro_solicitud, 8, 0), " +
            "CASE " +
            "   WHEN s.estado in (4, 5, 6, 7) THEN " +
            "       i.sucursal_rol || lpad(i.nro_inspeccion, 8, 0) || lpad(s.numero_cliente, 8, 0) " +
            "   ELSE '' " +
            "END workoder_id " +
            "FROM sol_inspecciones t, OUTER (inspecc:in_solicitud s, inspecc:in_estado_solic t1) " +
            "WHERE t.numero_cliente = ? " +
            "AND t.tarifa = 1 " +
            "AND s.numero_cliente = t.numero_cliente " +
            "AND s.nro_solicitud = t.nro_solicitud_inspeccion " +
            "AND t1.codigo_estado = s.estado " +
            "ORDER BY t.fecha_estado desc ";

    private static final String SEL_CONSULTA_T23 = "SELECT DISTINCT t.numero_cliente, s.tipo_extractor, t.type_of_selection, " +
            "s.sucursal, " +
            "s.estado, t1.descripcion, " +
            "t.cod_estado, t.desc_estado, " +
            "t.id_caso, t.fecha_estado, s.fecha_solicitud, " +
            "TRIM(s.tarifa) || LPAD(s.nro_solicitud, 8, 0), " +
            "CASE " +
            "   WHEN s.estado in (4, 5, 6, 7) THEN " +
            "       i.sucursal_rol || lpad(i.nro_inspeccion, 8, 0) || lpad(s.numero_cliente, 8, 0) " +
            "   ELSE '' " +
            "END workoder_id " +
            "FROM sol_inspecciones t, OUTER (inspect23:i3_solicitud s, inspecc:in_estado_solic t1) " +
            "WHERE t.numero_cliente = ? " +
            "AND t.tarifa IN (2, 3) " +
            "AND s.numero_cliente = t.numero_cliente " +
            "AND s.nro_solicitud = t.nro_solicitud_inspeccion " +
            "AND t1.codigo_estado = s.estado " +
            "ORDER BY t.fecha_estado desc ";


}
