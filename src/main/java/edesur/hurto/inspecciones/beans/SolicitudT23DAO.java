package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeSolicitudDTO;

import java.sql.*;

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


    public boolean InsertaSolicitud(InspeSolicitudDTO reg, Connection connection)throws  SQLException{
        String sTipoTarifa = Integer.toString(reg.getTipoTarifaT23());
        String sAux="";

        try(PreparedStatement stmt = connection.prepareStatement(INS_SOLICITUD)) {
            stmt.setInt(1, reg.getEstado());
            stmt.setLong(2, reg.getNumero_cliente());
            stmt.setString(3, reg.getSucursal_rol_solic().trim());
            stmt.setInt(4, reg.getPlan());
            stmt.setInt(5, reg.getRadio());
            stmt.setLong(6, reg.getCorrelativo_ruta());
            stmt.setString(7,"GLOBAL");
            stmt.setString(8, reg.getSucursal_rol_solic().trim());
            stmt.setString(9, reg.getDir_provincia().trim());
            stmt.setString(10, reg.getDir_nom_provincia().trim());
            stmt.setString(11, reg.getDir_partido().trim());
            //stmt.setString(12, reg.getDir_nom_partido().trim());
            stmt.setString(12, " ");
            //stmt.setString(13, reg.getDir_comuna().trim());
            stmt.setString(13, " ");
            stmt.setString(14, reg.getDir_nom_comuna().trim());
            stmt.setString(15, reg.getDir_cod_calle().trim());
            stmt.setString(16, reg.getDir_nom_calle().trim());
            stmt.setString(17, reg.getDir_numero().trim());
            stmt.setString(18, reg.getDir_piso().trim());
            stmt.setString(19, reg.getDir_depto().trim());
            stmt.setInt(20, reg.getDir_cod_postal());
            stmt.setString(21, reg.getDir_cod_entre().trim());
            stmt.setString(22, reg.getDir_nom_entre().trim());
            stmt.setString(23, reg.getDir_cod_entre1().trim());
            stmt.setString(24, reg.getDir_nom_entre1().trim());
            sAux=reg.getDir_observacion();
            if(sAux == null){
                //stmt.setString(25, " ");
                stmt.setNull(25, Types.VARCHAR);
            }else{
                stmt.setString(25, reg.getDir_observacion().trim());
            }
            //stmt.setString(26, reg.getDir_cod_barrio().trim());
            stmt.setString(26, " ");
            //stmt.setString(27, reg.getDir_nom_barrio().trim());
            stmt.setString(27, " ");
            stmt.setString(28, reg.getDir_manzana().trim());
            stmt.setLong(29, reg.getNro_ult_inspec());
            //stmt.setTimestamp(30, new Timestamp(reg.getFecha_ult_inspec().getTime()));
            stmt.setString(30, reg.getNombre().trim());
            stmt.setString(31, reg.getTip_doc().trim());
            stmt.setFloat(32, reg.getNro_doc());
            stmt.setString(33, reg.getTelefono());
            stmt.setString(34, reg.getMot_denuncia().trim());

            sAux=reg.getObservacion1();
            if(sAux== null){
                //stmt.setString(35, " ");
                stmt.setNull(35, Types.VARCHAR);

            }else{
                stmt.setString(35, reg.getObservacion1().trim());
            }

            stmt.setInt(36, reg.getTipoTarifaT23());
            stmt.setString(37, reg.getSucursalClienteT23().trim());

            stmt.executeUpdate();
        }

        return true;
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
            "AND codigo = 'INST23' " +
            "FOR UPDATE ";

    private static final String UPD_SECUEN = "UPDATE secuen SET " +
            "valor = valor +1 " +
            "WHERE codigo = 'INST23' " +
            "AND  sucursal = ? ";

    private static final String UPD_SOL_OCURRENCIA = "UPDATE inspect23:i3_solicitud SET " +
            "es_individual = 'S', " +
            "observacion2 = nvl(observacion2, '-') || '-se registro ocurrencia por ws Global' " +
            "WHERE nro_solicitud = ? ";

    private static final String EXEC_SUCURSAL_PADRE = "{call inspect23:sucursal_padre(?, ?) } ";

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
            "nro_ult_inspec, " +
            "nombre, tipo_doc, nro_doc, telefono, mot_denuncia, " +
            "observacion1 , tarifa, sucursal_cliente " +
            ")VALUES( " +
            "6, 'S', ?, TODAY, TODAY, ?, " +
            "?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?,  " +
            "?, ?, ?,  " +
            "?, ?, ?, ?, " +
            "?, ?, ?, ?, " +
            "?, ?, ?, " +
            "?, ?, ?,  " +
            "?, " +
            "?, ?, ?, ?, ?, " +
            "?, ?, ? ) ";

}
