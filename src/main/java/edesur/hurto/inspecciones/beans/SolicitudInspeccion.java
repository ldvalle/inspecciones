package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeSolicitudRequest;
import edesur.hurto.inspecciones.model.InspeSolicitudResponse;
import edesur.hurto.inspecciones.model.ClienteDTO;
import edesur.hurto.inspecciones.model.InspeSolicitudDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

public class SolicitudInspeccion {

    public DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public InspeSolicitudResponse CreateSolicitud(long idCaso, long nroCliente, String sCodMotivo){
        int iEstadoCliente=-1;
        int iTarifaCliente=-1;
        boolean MotivoValido=false;

        InspeSolicitudResponse regRes = new InspeSolicitudResponse();

        if(nroCliente < 80000000) {

            iTarifaCliente=1;
            try{
              iEstadoCliente = validaClienteT1(nroCliente);
              MotivoValido = validaMotivo(sCodMotivo);

              switch(iEstadoCliente){
                  case -1:
                    regRes.setCodigo_retorno("11");
                    regRes.setDescripcion_retorno("Cliente " + nroCliente + " de caso " + idCaso + " No Existe.");
                    break;
                    
                  case 0:
                    regRes.setCodigo_retorno("00");
                    regRes.setDescripcion_retorno("Cliente T1 activo ");
                    break;
  
                  default:
                    regRes.setCodigo_retorno("02");
                    regRes.setDescripcion_retorno("Cliente T1 no activo ");
                    break;
              }

              if (iEstadoCliente < 0){
                  RegistraRechazo(idCaso, nroCliente, sCodMotivo, iTarifaCliente,11, "Cliente No Existe");
                  return regRes;
              }

              if(!validaMotivo(sCodMotivo)){
                  regRes.setCodigo_retorno("12");
                  regRes.setDescripcion_retorno("Motivo Invalido ");
                  RegistraRechazo(idCaso, nroCliente, sCodMotivo, iTarifaCliente,12, "Motivo Invalido");
                  return regRes;
              }

              if(iEstadoCliente >=0) {
                  ClienteDTO regCliT1 = new ClienteDTO();

                  if (RegSolicitudT1(idCaso, nroCliente, sCodMotivo, iTarifaCliente, regCliT1)) {
                      regRes.setCodigo_retorno("1");
                      regRes.setDescripcion_retorno("Solicitud T1 " + idCaso + " Registrada");
                  } else {
                      regRes.setCodigo_retorno("0");
                      regRes.setDescripcion_retorno("Solicitud T1 " + idCaso + " NO Registrada");
                  }
              }
            }catch(Exception ex){
			     ex.printStackTrace();
			     throw new RuntimeException(ex);
		    }

          }else{
/*
            try {
                iTarifaCliente = validaClienteT23(nroCliente);

                if(iTarifaCliente > 1){
                    if (RegSolicitudT23(idCaso, nroCliente, sCodMotivo, iTarifaCliente)) {
                        regRes.setCodigo_retorno("1");
                        regRes.setDescripcion_retorno("Solicitud T1 " + idCaso + " Registrada");
                    } else {
                        regRes.setCodigo_retorno("0");
                        regRes.setDescripcion_retorno("Solicitud T1 " + idCaso + " NO Registrada");
                    }
                }else{
                    regRes.setCodigo_retorno("01");
                    regRes.setDescripcion_retorno("Cliente " + nroCliente + " de caso " + idCaso + " No Existe.");
                }

            }catch(Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
*/
            regRes.setCodigo_retorno("23");
            regRes.setDescripcion_retorno("Cliente T23");
        }

        return regRes;
    }

    private int validaClienteT1(long nroCliente) throws SQLException{
    int iEstadoCliente = -1;
    
        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement stmt = connection.prepareStatement(SEL_VALIDA_T1)) {
                stmt.setLong(1, nroCliente);
                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()){
                        iEstadoCliente=rs.getInt(1);
                    }
                }
            }
        }
        return iEstadoCliente;
    }

    private boolean validaMotivo(String sCodMotivo) throws SQLException{
        int iCant=0;

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement stmt = connection.prepareStatement(SEL_VALIDA_MOTIVO)) {
                stmt.setString(1, sCodMotivo.trim());

                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()){
                        iCant=rs.getInt(1);
                    }else{
                        iCant=-1;
                        return false;
                    }
                }
            }
        }

        if(iCant <= 0){
            return false;
        }

        return true;
    }

    private int validaClienteT23(long nroCliente) throws SQLException{
        int iSubTarifa =-1;

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement stmt = connection.prepareStatement(SEL_VALIDA_T23)) {
                stmt.setLong(1, nroCliente);
                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()){
                        iSubTarifa=rs.getInt(1);
                    }
                }
            }
        }

        return iSubTarifa;
    }

    private void RegistraRechazo(long idCaso, long nroCliente, String sCodMotivo, int tarifa,int codEstado, String descEstado)throws SQLException{
        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement stmt = connection.prepareStatement(INS_RECHAZO)) {
                stmt.setLong(1, idCaso);
                stmt.setLong(2, nroCliente);
                stmt.setString(3, sCodMotivo.trim());
                stmt.setInt(4, tarifa);
                stmt.setInt(5, codEstado);
                stmt.setString(6, descEstado.trim());

                stmt.executeUpdate();
            }
        }
    }

    private Boolean RegSolicitudT1(long idCaso, long nroCliente, String sCodMotivo, int tarifa, ClienteDTO regCli) throws SQLException{
    
        try(Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try(PreparedStatement stmt = connection.prepareStatement(INS_PEDIDO)) {
                stmt.setLong(1, idCaso);
                stmt.setLong(2, nroCliente);
                stmt.setString(3, sCodMotivo.trim());
                stmt.setInt(4, tarifa);
                stmt.setInt(5, 0);
                
                stmt.executeUpdate();

                if(tarifa==1){

                    if(!ProcesoT1(idCaso, nroCliente, sCodMotivo, regCli, connection)){
                        connection.rollback();
                        return false;
                    }

                }else{
                    // Proceso de T23
                }
            }
            connection.commit();
        }
        return true;
    }

    private ClienteDTO cargaClienteT1(Long nroCliente) throws SQLException{
        ClienteDTO reg = new ClienteDTO();

        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(SEL_CLIENTE_T1, ResultSet.CONCUR_READ_ONLY)) {
                stmt.setLong(1, nroCliente);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        reg.setSucursal(rs.getString(1));
                        reg.setSector(rs.getInt(2));
                        reg.setZona(rs.getInt(3));
                        reg.setCorrelativo_ruta(rs.getLong(4));
                        reg.setProvincia(rs.getString(5));
                        reg.setNom_provincia(rs.getString(6));
                        reg.setPartido(rs.getString(7));
                        reg.setNom_partido(rs.getString(8));
                        reg.setComuna(rs.getString(9));
                        reg.setNom_comuna(rs.getString(10));
                        reg.setCod_calle(rs.getString(11));
                        reg.setNom_calle(rs.getString(12));
                        reg.setNro_dir(rs.getString(13));
                        reg.setPiso_dir(rs.getString(14));
                        reg.setDepto_dir(rs.getString(15));
                        reg.setCod_entre(rs.getString(16));
                        reg.setNom_entre(rs.getString(17));
                        reg.setCod_entre1(rs.getString(18));
                        reg.setNom_entre1(rs.getString(19));
                        reg.setBarrio(rs.getString(20));
                        reg.setNom_barrio(rs.getString(21));
                        reg.setNombre(rs.getString(22));
                        reg.setTip_doc(rs.getString(23));
                        reg.setNro_doc(rs.getFloat(24));
                        reg.setTelefono(rs.getString(25));
                        reg.setEstado_cliente(rs.getInt(26));
                        reg.setCod_postal(rs.getInt(27));
                        reg.setObs_dir(rs.getString(28));
                        reg.setManzana(rs.getString(29));
                        reg.setNumero_medidor(rs.getLong(30));
                        reg.setMarca_medidor(rs.getString(31));
                        reg.setModelo_medidor(rs.getString(32));
                    }
                }
            }
        }
        return reg;
    }

    private boolean ProcesoT1(long idCaso, long nroCliente, String sCodMotivo, ClienteDTO regCli, Connection connection) throws SQLException{
    int iEstado=0;
    String sComentario="";
    Long    lNroUltimaSol;
    int     iTipoExtractor;
    int     iDiffDias;
    int     iEstadoUltimaSol;

        //Verificar que no tenga Individual Pendiente
        if(!IndivPteT1(nroCliente, connection)){
            InspeSolicitudDTO regUltimaSol = new InspeSolicitudDTO();

            //Levantar ultima solicitud
            regUltimaSol = CargaUltimaSol(nroCliente, connection);
            lNroUltimaSol= regUltimaSol.getNro_medidor();
            if(lNroUltimaSol>0){
                iTipoExtractor=regUltimaSol.getTipo_extractor();
                iEstadoUltimaSol=regUltimaSol.getEstado();
                iDiffDias=regUltimaSol.getDifDiasEntre();

                //Si tiene masiva solicitada se anexa a la individual
                if(iTipoExtractor != 6 && iEstadoUltimaSol==1){
                    iEstado=2;
                    if(!AnexaInspeccion(nroCliente, sCodMotivo, regUltimaSol, iEstado, connection)){
                        return false;
                    }
                }

                //Si tiene una inspe pendiente, se registra la ocurrencia

                //Si tiene ultima inspe finalizada, se evalua los N dias

            }

            //Dependiendo del estado se genera solicitud o inspeccion

        }else{
            iEstado=11;
            sComentario="Ya tiene Inspección Individual Pendiente.";
        }

        //Actualizar Caso con iEstado y sComentario

        return true;
    }

    private Boolean IndivPteT1(long nroCliente, Connection connection) throws SQLException{
        int iCant=0;
        try(PreparedStatement stmt = connection.prepareStatement(SEL_INDIV_SOL)) {
            stmt.setLong(1, nroCliente);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    iCant=rs.getInt(1);
                }else{
                    iCant = -1;
                }
            }
        }

        if(iCant <= 0){
            return false;
        }else{
            return true;
        }
    }

    private InspeSolicitudDTO CargaUltimaSol(Long nroCliente, Connection connection)throws SQLException{
        InspeSolicitudDTO reg = new InspeSolicitudDTO();

        try(PreparedStatement stmt = connection.prepareStatement(SEL_ULTIMA_SOL_T1)) {
            stmt.setLong(1, nroCliente);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    reg.setNro_solicitud(rs.getLong(1));
                    reg.setTipo_extractor(rs.getInt(2));
                    reg.setEstado(rs.getInt(3));
                    reg.setDifDiasEntre(rs.getInt(4));
                    reg.setSucursal_rol_solic(rs.getString(5));
                    reg.setNumero_cliente(rs.getLong(6));
                }else {
                    reg.setNro_solicitud(Long.parseLong("0"));
                }
             }
        }
        return reg;
    }

    private boolean AnexaInspeccion(Long nroCliente, String sCodMotivo, InspeSolicitudDTO regUltimaSol, int iEstado, Connection connection)throws SQLException{
        long lNroInspeccion = 0;

        try(PreparedStatement stmt = connection.prepareStatement(UPD_SOL_ANEXADA)) {
            stmt.setInt(1, iEstado);
            stmt.setString(2, sCodMotivo.trim());
            stmt.setLong(3, regUltimaSol.getNro_solicitud());

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


    private boolean ProcesoT23(long nroCliente, int iTarifa, String sCodMotivo, Connection connection){

        //Verificar que no tenga Individual Pendiente
        //Si tiene querella agregar en comentarios
        //Si tiene CNR agregar a comentarios.
        //Si tiene accion tomada agregar en comentarios
        //Levantar ultima solicitud
        //Si tiene masiva solicitada se anexa a la individual
        //Si tiene una inspe pendiente, se registra la ocurrencia
        //Si tiene ultima inspe finalizada, se evalua los N dias
        //Dependiendo del estado se genera solicitud o inspeccion
        //Actualizar Caso
        return true;
    }


    private static final String SEL_VALIDA_T1 = "SELECT estado_cliente FROM cliente " +
            "WHERE numero_cliente = ? ";

    private static final String SEL_VALIDA_MOTIVO="SELECT COUNT(*) FROM tabla " +
            "WHERE nomtabla = 'MOTHUR' " +
            "AND codigo = ? " +
            "AND sucursal ='0000' " +
            "AND fecha_activacion <= TODAY " +
            "AND (fecha_desactivac IS NULL OR fecha_desactivac > TODAY) ";

    private static final String SEL_VALIDA_T23 = "SELECT t3_tipo_cliente FROM t3_cliente " +
            "WHERE t3_numero_cliente = ? ";
            
    private static final String INS_PEDIDO = "INSERT INTO sol_inspecciones ( " +
            "id_caso, " + 
            "numero_cliente, " +
            "cod_motivo, " +
            "tarifa, " +
            "cod_estado, " +
            "fecha_estado " +
            ")VALUES( " +
            "?, ?, ?, ?, ?, TODAY) ";    

    private static final String INS_RECHAZO = "INSERT INTO sol_inspecciones ( " +
            "id_caso, " +
            "numero_cliente, " +
            "cod_motivo, " +
            "tarifa, " +
            "cod_estado, " +
            "desc_estado, " +
            "fecha_estado " +
            ")VALUES( " +
            "?, ?, ?, ?, ?, TODAY) ";

    private static final String SEL_INDIV_SOL = "SELECT COUNT(*) FROM inspecc:in_solicitud " +
            "WHERE numero_cliente = ? " +
            "AND estado NOT IN (3, 7) " +
            "AND tipo_extractor = 6 ";

    private static final String SEL_CLIENTE_T1 = "SELECT c.sucursal, c.sector, c.zona, " +
            "c.correlativo_ruta, c.provincia, c.nom_provincia, " +
            "c.partido, c.nom_partido, c.comuna, " +
            "c.nom_comuna, c.cod_calle, c.nom_calle, " +
            "c.nro_dir, c.piso_dir, c.depto_dir, " +
            "c.cod_entre, c.nom_entre, c.cod_entre1, " +
            "c.nom_entre1, c.barrio, c.nom_barrio, " +
            "c.nombre, c.tip_doc, c.nro_doc, " +
            "c.telefono, c.estado_cliente, c.cod_postal, " +
            "c.obs_dir, c.manzana " +
            "m.numero_medidor, m.marca_medidor, m.modelo_medidor " +
            "FROM cliente c, OUTER medid m " +
            "WHERE c.numero_cliente = ? " +
            "AND m.numero_cliente = c.numero_cliente " +
            "AND m.estado = 'I' ";

    private static final String SEL_ULTIMA_SOL_T1 = "SELECT s1.nro_solicitud, s1.tipo_extractor, s1.estado,  " +
            "today - s1.fecha_estado dif_dias, " +
            "s1.sucursal_rol_solic, s1.numero_cliente " +
            "FROM inspecc:in_solicitud s1 " +
            "WHERE s1.numero_cliente = ? " +
            "AND s1.fecha_estado = (SELECT MAX(s2.fecha_estado) " +
            "   FROM inspecc:in_solicitud s2 " +
            "   WHERE s2.numero_cliente = s2.numero_cliente) ";

    private static final String UPD_SOL_ANEXADA = "UPDATE in_solicitud SET " +
            "es_individual = 'S', " +
            "tipo_extractor = 6, " +
            "mot_denuncia = ?, " +
            "observacion2 = observacion2 + ' Anexada por ws Global' " +
            "estado = ? " +
            "WHERE nro_solicitud = ? ";

    private static final String INS_INSPE_ANEXADA = "INSERT INTO in_inspeccion ( sucursal_rol, nro_inspeccion, " +
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

}

