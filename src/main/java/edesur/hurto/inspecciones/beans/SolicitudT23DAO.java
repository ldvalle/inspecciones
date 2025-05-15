package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeSolicitudDTO;

import java.sql.*;

public class SolicitudT23DAO {

    public InspeSolicitudDTO getUltimaSolicitud(long nroCliente, Connection conn)throws SQLException{
        InspeSolicitudDTO reg = new InspeSolicitudDTO();
        long auxNroCliente=0;

        if(nroCliente > 80000000 && nroCliente < 80500000) {
            auxNroCliente = nroCliente - 80000000;
        }else{
            auxNroCliente = nroCliente;
        }

        try(PreparedStatement stmt = conn.prepareStatement(SEL_ULTIMA_SOL_T1)) {
            //stmt.setLong(1, nroCliente);
            stmt.setLong(1, auxNroCliente);
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

    public boolean AnexaInspeccion(Long nroCliente, String sCodMotivo, String typeOfSelection, InspeSolicitudDTO regUltimaSol, int iEstado, Connection connection)throws SQLException{
        long lNroInspeccion = 0;
        Long lNroSolAnterior = regUltimaSol.getNro_solicitud();
        long auxNroCliente=0;
        String sql="";

        if(nroCliente > 80000000 && nroCliente < 80500000) {
            auxNroCliente = nroCliente - 80000000;
        }else{
            auxNroCliente = nroCliente;
        }

        if(typeOfSelection.trim().equals("Ty3ND")){
            sql = UPD_SOL_ANEXADA_IND;
        }else{
            sql = UPD_SOL_ANEXADA_GRP;
        }

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
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
            //stmt.setLong(4,nroCliente);
            stmt.setLong(4,auxNroCliente);

            stmt.executeUpdate();
        }

        return true;
    }

    public boolean RegistraOcurrencia(Long nroSolicitud, String typeOfSelection, Connection connection)throws  SQLException{
        String sql="";

        if(typeOfSelection.trim().equals("Ty3ND")){
            sql = UPD_SOL_OCURRENCIA_IND;
        }else{
            sql = UPD_SOL_OCURRENCIA_GRP;
        }

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
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


    public boolean InsertaSolicitud(InspeSolicitudDTO reg, String typeOfSelection, Connection connection)throws  SQLException{
        String sTipoTarifa = Integer.toString(reg.getTipoTarifaT23());
        String sAux="";
        int iTipoExtractor;
        String esIndividual="";
        String esGrupo="";

        if(typeOfSelection.trim().equals("Ty3ND")){
            iTipoExtractor=6;
            esIndividual="S";
        }else{
            iTipoExtractor=5;
            esGrupo="S";
        }

        try(PreparedStatement stmt = connection.prepareStatement(INS_SOLICITUD)) {
            stmt.setInt(1, reg.getEstado());
            stmt.setLong(2, reg.getNumero_cliente());
            stmt.setString(3, reg.getSucursal_rol_solic());
            stmt.setInt(4, reg.getPlan());
            stmt.setInt(5, reg.getRadio());
            stmt.setLong(6, reg.getCorrelativo_ruta());
            stmt.setString(7,"GLOBAL");
            stmt.setString(8, reg.getSucursal_rol_solic());
            stmt.setString(9, reg.getDir_provincia());
            stmt.setString(10, reg.getDir_nom_provincia());
            stmt.setString(11, reg.getDir_partido());
            //stmt.setString(12, reg.getDir_nom_partido().trim());
            stmt.setString(12, " ");
            //stmt.setString(13, reg.getDir_comuna().trim());
            stmt.setString(13, " ");
            stmt.setString(14, reg.getDir_nom_comuna());
            stmt.setString(15, reg.getDir_cod_calle());
            stmt.setString(16, reg.getDir_nom_calle());
            stmt.setString(17, reg.getDir_numero());
            stmt.setString(18, reg.getDir_piso());
            stmt.setString(19, reg.getDir_depto());
            //stmt.setInt(20, reg.getDir_cod_postal());
            stmt.setObject(20, reg.getDir_cod_postal(), Types.INTEGER);
            stmt.setString(21, reg.getDir_cod_entre());
            stmt.setString(22, reg.getDir_nom_entre());
            stmt.setString(23, reg.getDir_cod_entre1());
            stmt.setString(24, reg.getDir_nom_entre1());
            sAux=reg.getDir_observacion();
            if(sAux == null){
                //stmt.setString(25, " ");
                stmt.setNull(25, Types.VARCHAR);
            }else{
                stmt.setString(25, reg.getDir_observacion());
            }
            //stmt.setString(26, reg.getDir_cod_barrio().trim());
            stmt.setString(26, " ");
            //stmt.setString(27, reg.getDir_nom_barrio().trim());
            stmt.setString(27, " ");
            stmt.setString(28, reg.getDir_manzana());
            stmt.setLong(29, reg.getNro_ult_inspec());
            //stmt.setTimestamp(30, new Timestamp(reg.getFecha_ult_inspec().getTime()));
            stmt.setString(30, reg.getNombre());
            stmt.setString(31, reg.getTip_doc());
            stmt.setFloat(32, reg.getNro_doc());
            stmt.setString(33, reg.getTelefono());
            stmt.setString(34, reg.getMot_denuncia());

            sAux=reg.getObservacion1();
            if(sAux== null){
                //stmt.setString(35, " ");
                stmt.setNull(35, Types.VARCHAR);

            }else{
                stmt.setString(35, reg.getObservacion1().trim());
            }

            stmt.setInt(36, reg.getTipoTarifaT23());
            stmt.setString(37, reg.getSucursalClienteT23());
            stmt.setInt(38, iTipoExtractor);
            stmt.setString(39, esIndividual);
            stmt.setString(40, esGrupo);

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

    private static final String UPD_SOL_ANEXADA_IND = "UPDATE inspect23:i3_solicitud SET " +
            "es_individual = 'S', " +
            "tipo_extractor = 6, " +
            "mot_denuncia = ?, " +
            "observacion2 = nvl(observacion2, '-') || '-Anexada por ws Global', " +
            "estado = ?, " +
            "fecha_estado = TODAY " +
            "WHERE nro_solicitud = ? ";

    private static final String UPD_SOL_ANEXADA_GRP = "UPDATE inspect23:i3_solicitud SET " +
            "es_grupo = 'S', " +
            "tipo_extractor = 5, " +
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
            "AND codigo = 'INST23' ";

    private static final String UPD_SECUEN = "UPDATE secuen SET " +
            "valor = valor +1 " +
            "WHERE codigo = 'INST23' " +
            "AND  sucursal = ? ";

    private static final String UPD_SOL_OCURRENCIA_IND = "UPDATE inspect23:i3_solicitud SET " +
            "es_individual = 'S', " +
            "observacion2 = nvl(observacion2, '-') || '-se registro ocurrencia por ws Global' " +
            "WHERE nro_solicitud = ? ";

    private static final String UPD_SOL_OCURRENCIA_GRP = "UPDATE inspect23:i3_solicitud SET " +
            "es_grupo = 'S', " +
            "observacion2 = nvl(observacion2, '-') || '-se registro ocurrencia por ws Global' " +
            "WHERE nro_solicitud = ? ";

    private static final String EXEC_SUCURSAL_PADRE = "{call inspect23:sucursal_padre(?, ?) } ";

    private static final String INS_SOLICITUD = "INSERT INTO inspect23:i3_solicitud ( " +
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
            "observacion1 , tarifa, sucursal_cliente, tipo_extractor, es_individual, es_grupo " +
            ")VALUES( " +
            "?, TODAY, TODAY, ?, " +
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
            "?, ?, ?, ?, ?, ? ) ";

}
