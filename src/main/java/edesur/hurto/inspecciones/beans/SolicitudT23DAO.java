package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeSolicitudDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SolicitudT23DAO {

    public InspeSolicitudDTO getUltimaSolicitud(long nroCliente, Connection conn)throws SQLException{
        InspeSolicitudDTO reg = new InspeSolicitudDTO();

        try(PreparedStatement stmt = conn.prepareStatement(SEL_ULTIMA_SOL_T1)) {
            stmt.setLong(1, nroCliente);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    reg.setNro_solicitud(rs.getLong(1));
                    reg.setTipo_extractor(rs.getInt(2));
                    reg.setEstado(rs.getInt(3));
                    reg.setDifDiasEntre(rs.getInt(4));
                    reg.setSucursal_rol_solic(rs.getString(5));
                    reg.setNumero_cliente(rs.getLong(6));
                    reg.setEjecuto_inspeccion(rs.getString(7));
                }else {
                    reg.setNro_solicitud(Long.parseLong("0"));
                    reg.setEjecuto_inspeccion("N");
                }
            }
        }
        return reg;
    }

    public boolean AnexaInspeccion(Long nroCliente, String sCodMotivo, InspeSolicitudDTO regUltimaSol, int iEstado, Connection connection)throws SQLException{
        long lNroInspeccion = 0;
        Long lNroSolAnterior = regUltimaSol.getNro_solicitud();

        try(PreparedStatement stmt = connection.prepareStatement(UPD_SOL_ANEXADA)) {
            stmt.setString(1, sCodMotivo.trim());
            stmt.setInt(2, iEstado);
            stmt.setLong(3, lNroSolAnterior);

            stmt.executeUpdate();
        }

        try(PreparedStatement stmt=connection.prepareStatement(SEL_SECUEN)){
            stmt.setString(1, regUltimaSol.getSucursal_rol_solic());

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    lNroInspeccion=rs.getLong(1);
                }else {
                    return false;
                }
            }
        }

        try(PreparedStatement stmt = connection.prepareStatement(UPD_SECUEN)) {
            stmt.setString(1, regUltimaSol.getSucursal_rol_solic());

            stmt.executeUpdate();
        }

        try(PreparedStatement stmt = connection.prepareStatement(INS_INSPE_ANEXADA)) {
            stmt.setString(1,regUltimaSol.getSucursal_rol_solic());
            stmt.setLong(2,lNroInspeccion);
            stmt.setLong(3, regUltimaSol.getNro_solicitud());
            stmt.setLong(4,nroCliente);

            stmt.executeUpdate();
        }

        return true;
    }

    public boolean RegistraOcurrencia(Long nroSolicitud, Connection connection)throws  SQLException{
        try(PreparedStatement stmt = connection.prepareStatement(UPD_SOL_OCURRENCIA)) {
            stmt.setLong(1, nroSolicitud);

            stmt.executeUpdate();
        }

        return true;
    }

    public String getSucursalPadre(String sSucursal, int iTarifa, Connection connection)throws  SQLException{
        String sSucurPadre="";

        try(PreparedStatement stmt=connection.prepareStatement(EXEC_SUCURSAL_PADRE)){
            stmt.setString(1, sSucursal);
            stmt.setInt(2, iTarifa);

            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    sSucurPadre=rs.getString(1);
                }
            }
        }
        return sSucurPadre;
    }


    private static final String SEL_ULTIMA_SOL_T1 = "SELECT s1.nro_solicitud, s1.tipo_extractor, s1.estado,  " +
            "today - s1.fecha_estado dif_dias, " +
            "s1.sucursal_rol_solic, s1.numero_cliente, NVL(i.ejecuto_inspeccion, 'N') inspec_ejec " +
            "FROM inspect23:i3_solicitud s1, OUTER inspect23:i3_inspeccion i " +
            "WHERE s1.numero_cliente = ? " +
            "AND s1.fecha_estado = (SELECT MAX(s2.fecha_estado) " +
            "   FROM inspect23:i3_solicitud s2 " +
            "   WHERE s2.numero_cliente = s1.numero_cliente) " +
            "AND i.nro_solicitud = s1.nro_solicitud ";

    private static final String UPD_SOL_ANEXADA = "UPDATE inspect23:i3_solicitud SET " +
            "es_individual = 'S', " +
            "tipo_extractor = 6, " +
            "mot_denuncia = ?, " +
            "observacion2 = nvl(observacion2, '-') || '-Anexada por ws Global', " +
            "estado = ?, " +
            "fecha_estado = TODAY " +
            "WHERE nro_solicitud = ? ";

    private static final String INS_INSPE_ANEXADA = "INSERT INTO inspect23:i3_inspeccion ( sucursal_rol, nro_inspeccion, " +
            "nro_solicitud, fecha_generacion, numero_cliente " +
            ")VALUES( ?, ?, ?, TODAY, ?) ";

    private static final String SEL_SECUEN = "SELECT valor + 1 FROM secuen " +
            "WHERE sucursal = ? " +
            "AND codigo = 'INSPEC' " +
            "FOR UPDATE ";

    private static final String UPD_SECUEN = "UPDATE secuen SET " +
            "valor = valor +1 " +
            "WHERE codigo = 'INSPEC' " +
            "AND  sucursal = ? ";

    private static final String UPD_SOL_OCURRENCIA = "UPDATE inspect23:i3_solicitud SET " +
            "es_individual = 'S', " +
            "observacion2 = nvl(observacion2, '-') || '-se registro ocurrencia por ws Global' " +
            "WHERE nro_solicitud = ? ";

    private static final String EXEC_SUCURSAL_PADRE = "inspect23:sucursal_padre(?, ?) ";

    private static final String INS_SOLICITUD = "INSERT INTO inspect23:i3_solicitud ( tipo_extractor, es_individual, " +
            "estado, fecha_estado, fecha_solicitud, numero_cliente, " +
            "sucursal, plan, radio, correlativo_ruta, " +
            "rol_solicitud, sucursal_rol_solic, " +
            "dir_provincia, dir_nom_provincia, dir_partido, " +
            "dir_nom_partido, dir_comuna, dir_nom_comuna, " +
            "dir_cod_calle, dir_nom_calle, dir_numero, dir_piso, " +
            "dir_depto, dir_cod_postal, dir_cod_entre, dir_nom_entre, " +
            "dir_cod_entre1, dir_nom_entre1, dir_observacion, " +
            "dir_cod_barrio, dir_nom_barrio, dir_manzana, " +
            "nro_ult_inspec, fecha_ult_inspec, " +
            "nombre, tipo_doc, nro_doc, telefono, mot_denuncia, " +
            "observacion1 , tarifa, sucursal_cliente " +
            ")VALUES( " +
            "6, 'S', 1, TODAY, TODAY, ?, " +
            "?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?,  " +
            "?, ?, ?,  " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, " +
            "?, ?, ?,  " +
            "?, ?, " +
            "?, ?, ?, ?, ?, " +
            "?, ?, ? ) ";
}
