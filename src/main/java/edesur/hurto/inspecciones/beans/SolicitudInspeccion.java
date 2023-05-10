package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeSolicitudRequest;
import edesur.hurto.inspecciones.model.InspeSolicitudResponse;
import edesur.hurto.inspecciones.model.ClienteDTO;
import edesur.hurto.inspecciones.model.InspeSolicitudDTO;

import javax.sql.DataSource;
import java.sql.*;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

public class SolicitudInspeccion {

    public DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public InspeSolicitudResponse CreateSolicitud(String idCaso, long nroCliente, String typeOfSelection){
        int iEstadoCliente=-1;
        int iTarifaCliente=-1;
        String sCodMotivo = "D15";
        boolean MotivoValido=false;

        InspeSolicitudResponse regRes = new InspeSolicitudResponse();

        if(!idCaso.substring(0,3).equals("AR_")){
            regRes.setCodigo_retorno("KO");
            regRes.setDescripcion_retorno("IdOpportunity Invalid For Argentina");
            return regRes;
        }

        if(nroCliente < 80000000) {

            iTarifaCliente=1;
            try{
              iEstadoCliente = validaClienteT1(nroCliente);
              //MotivoValido = validaMotivo(sCodMotivo);

              switch(iEstadoCliente){
                  case -1:
                    regRes.setCodigo_retorno("KO");
                    regRes.setDescripcion_retorno("Cliente " + nroCliente + " de caso " + idCaso + " No Existe.");
                    break;
                    
                  case 0:
                    regRes.setCodigo_retorno("KO");
                    regRes.setDescripcion_retorno("Cliente T1 activo ");
                    break;
  
                  default:
                    regRes.setCodigo_retorno("KO");
                    regRes.setDescripcion_retorno("Cliente T1 no activo ");
                    break;
              }

              if (iEstadoCliente < 0){
                  RegistraRechazo(idCaso, nroCliente, sCodMotivo, iTarifaCliente,11, "Cliente No Existe", typeOfSelection);
                  return regRes;
              }

              if(!validaMotivo(sCodMotivo)){
                  regRes.setCodigo_retorno("KO");
                  regRes.setDescripcion_retorno("Codigo de Motivo Invalido ");
                  RegistraRechazo(idCaso, nroCliente, sCodMotivo, iTarifaCliente,12, "Motivo Invalido", typeOfSelection);
                  return regRes;
              }

              if(iEstadoCliente >=0) {
                  ClienteDTO regCliT1 = new ClienteDTO();

                  regCliT1  = cargaClienteT1(nroCliente);

                  if(regCliT1.getSucursal().trim().equals("9999")){
                      regRes.setCodigo_retorno("KO");
                      regRes.setDescripcion_retorno("Cliente con datos inválidos para inspeccion ");
                      RegistraRechazo(idCaso, nroCliente, sCodMotivo, iTarifaCliente,15, "Cliente con datos inválidos para inspeccion", typeOfSelection);
                      return regRes;

                  }

                  if (RegSolicitudT1(idCaso, nroCliente, sCodMotivo, typeOfSelection, iTarifaCliente, regCliT1)) {
                      regRes.setCodigo_retorno("OK");
                      regRes.setDescripcion_retorno("Solicitud T1 " + idCaso + " Registrada");
                  } else {
                      regRes.setCodigo_retorno("KO");
                      regRes.setDescripcion_retorno("Solicitud T1 " + idCaso + " NO Registrada");
                  }
              }
            }catch(Exception ex){
			     ex.printStackTrace();
			     throw new RuntimeException(ex);
		    }
        }else {
            regRes.setCodigo_retorno("KO");
            regRes.setDescripcion_retorno("Caso " + idCaso + " Entro como T1 pero es un T23");
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

    private void RegistraRechazo(String idCaso, long nroCliente, String sCodMotivo, int tarifa,int codEstado, String descEstado, String sTipoSeleccion)throws SQLException{
        if(codEstado==0 && (descEstado.trim().equals("") || descEstado == null))
            descEstado="Error Interno";

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement stmt = connection.prepareStatement(INS_RECHAZO)) {
                stmt.setString(1, idCaso);
                stmt.setLong(2, nroCliente);
                stmt.setString(3, sCodMotivo.trim());
                stmt.setInt(4, tarifa);
                stmt.setInt(5, codEstado);
                stmt.setString(6, descEstado.trim());
                stmt.setString(7, sTipoSeleccion.trim());

                stmt.executeUpdate();
            }
        }
    }

    private Boolean RegSolicitudT1(String idCaso, long nroCliente, String sCodMotivo, String typeOfSelection, int tarifa, ClienteDTO regCli) throws SQLException{
    
        try(Connection connection = dataSource.getConnection()) {
            //connection.setAutoCommit(false);
            try(PreparedStatement stmt = connection.prepareStatement(INS_PEDIDO)) {
                stmt.setString(1, idCaso);
                stmt.setLong(2, nroCliente);
                stmt.setString(3, sCodMotivo.trim());
                stmt.setString(4, typeOfSelection.trim());
                stmt.setInt(5, tarifa);
                stmt.setInt(6, 0);
                
                stmt.executeUpdate();

                if(tarifa==1){

                    if(!ProcesoT1(idCaso, nroCliente, sCodMotivo, typeOfSelection, regCli, connection)){
                        //connection.rollback();
                        return false;
                    }

                }else{
                    // Proceso de T23
                }
            }
            //connection.commit();
        }
        return true;
    }

    private ClienteDTO cargaClienteT1(Long nroCliente) throws SQLException{
        ClienteDTO reg = new ClienteDTO();

        try(Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(SEL_CLIENTE_T1)) {
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
                        reg.setnDiasConfig(rs.getInt(33));
                    }else{
                        reg.setSucursal("9999");
                    }
                }
            }
        }
        return reg;
    }

    private boolean ProcesoT1(String idCaso, long nroCliente, String sCodMotivo, String typeOfSelection, ClienteDTO regCli, Connection connection) throws SQLException{
    int     iEstado=99;
    String  sComentario="";
    Long    lNroUltimaSol;
    long    lNroSolicitud;
    int     iTipoExtractor;
    int     iDiffDias=0;
    int     iEstadoUltimaSol;
    int     iDiasParametro=0;
    InspeSolicitudDTO regSol;

        lNroSolicitud=0;
        //Verificar que no tenga Individual Pendiente
        if(!IndivPteT1(nroCliente, connection)){
            InspeSolicitudDTO regUltimaSol = new InspeSolicitudDTO();
            //Levantar ultima solicitud
            regUltimaSol = CargaUltimaSol(nroCliente, connection);
            lNroUltimaSol= regUltimaSol.getNro_solicitud();
            if(lNroUltimaSol>0) {
                iTipoExtractor = regUltimaSol.getTipo_extractor();
                iEstadoUltimaSol = regUltimaSol.getEstado();
                iDiffDias = regUltimaSol.getDifDiasEntre();
                //Si tiene masiva solicitada se anexa a la individual
                if (iTipoExtractor != 6 && iEstadoUltimaSol == 1) {
                    iEstado = 2;
                    if (!AnexaInspeccion(nroCliente, sCodMotivo, typeOfSelection, regUltimaSol, iEstado, connection)) {
                        return false;
                    }
                    sComentario = "Se anexó a solicitud masiva solicitada.";
                    lNroSolicitud = lNroUltimaSol;
                }
                //Si tiene una inspe pendiente, se registra la ocurrencia
                if (iEstadoUltimaSol != 1 && iEstadoUltimaSol != 3 && iEstadoUltimaSol != 7) {
                    iEstado = iEstadoUltimaSol;
                    if (!RegistraOcurrencia(typeOfSelection, lNroUltimaSol, connection)) {
                        return false;
                    }
                    sComentario = "Se registra ocurrencia con ultima solicitud pendiente.";
                    lNroSolicitud = lNroUltimaSol;
                }
                //Si tiene ultima inspe finalizada, se evalua los N dias
                if (iEstadoUltimaSol == 7 && !(regUltimaSol.getEjecuto_inspeccion().trim().equals("N"))) {
                    iDiffDias = regUltimaSol.getDifDiasEntre();
                    iDiasParametro = regCli.getnDiasConfig();

                    if (iDiffDias < iDiasParametro) {
                        iEstado = 0;
                        sComentario = "No pasaron los " + iDiasParametro + " entre inspecciones.";
                        lNroSolicitud=0;
                    } else {
                        iEstado = 1;
                    }
                }
            }

            // si tiene CNR pasarlo a estado 8 sinó queda en estado 1
            if(iEstado != 0) {
                if (TieneCNR(nroCliente, connection)) {
                    iEstado = 8;
                    sComentario = "Insp.solicitada pendiente por CNR vigente.";
                } else {
                    iEstado = 1;
                    sComentario = "Inspeccion solicitada.";
                }
            }
            //Dependiendo del estado se genera solicitud
            if(iEstado==1 || iEstado==8){
                if(typeOfSelection.trim().equals("Ty3ND")){
                    iEstado=8;
                }else{
                    iEstado=1;
                }

                if(iEstado==8){
                    sComentario +=" Ruta Lectura Asignada";
                }
                regSol=ArmaNvaSolicitud(nroCliente, sCodMotivo, iEstado, sComentario, regCli);
                if(!GrabaNvaSol(regSol, typeOfSelection, connection)){
                    return false;
                }
                // Lee la ultima solicitud para completar el nro.Sol
                lNroSolicitud=0;
                lNroSolicitud=getNvaSolicitud(nroCliente, sCodMotivo, connection);
            }

        }else{
            iEstado=14;
            sComentario="Ya tiene Inspección Pendiente de Carga.";
        }

        //Actualizar Caso con iEstado y sComentario
        if(!ActualizarCaso(lNroSolicitud, nroCliente, idCaso, iEstado, sComentario, connection)){
            return false;
        }
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
                    reg.setEjecuto_inspeccion(rs.getString(7));
                }else {
                    reg.setNro_solicitud(Long.parseLong("0"));
                    reg.setEjecuto_inspeccion("N");
                }
             }
        }
        return reg;
    }

    private boolean AnexaInspeccion(Long nroCliente, String sCodMotivo, String typeOfSelection, InspeSolicitudDTO regUltimaSol, int iEstado, Connection connection)throws SQLException{
        long lNroInspeccion = 0;
        int iTipoExtractor;
        String sql="";
        Long lNroSolAnterior = regUltimaSol.getNro_solicitud();

        if(typeOfSelection.trim().equals("Ty3ND")){
            iTipoExtractor=6;
            sql = UPD_SOL_ANEXADA_IND;
        }else{
            iTipoExtractor=5;
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
            stmt.setLong(4,nroCliente);

            stmt.executeUpdate();
        }

        return true;
    }

    private boolean RegistraOcurrencia(String typeOfSelection, Long nroSolicitud, Connection connection)throws  SQLException{
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

    private boolean ActualizarCaso(long lNroSolicitud,long lNroCliente, String idCaso, int iEstado, String sComentario, Connection connection)throws SQLException{

        try(PreparedStatement stmt = connection.prepareStatement(UPD_CASO)) {
            stmt.setLong(1, lNroSolicitud);
            stmt.setInt(2, iEstado);
            stmt.setString(3, sComentario.trim());
            stmt.setString(4,idCaso);
            stmt.setLong(5, lNroCliente);
            stmt.executeUpdate();
        }
        return true;
    }

    private boolean TieneCNR(long lNroCliente, Connection connection)throws SQLException{
        int iCant=0;
        try(PreparedStatement stmt = connection.prepareStatement(SEL_CNR)) {
            stmt.setLong(1, lNroCliente);
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

    private InspeSolicitudDTO ArmaNvaSolicitud(long nroCliente, String sCodMotivo, int iEstado, String sObserva, ClienteDTO regCli){
        InspeSolicitudDTO reg = new InspeSolicitudDTO();

        reg.setEstado(iEstado);
        reg.setNumero_cliente(nroCliente);
        reg.setSucursal(regCli.getSucursal().trim());
        reg.setPlan(regCli.getSector());
        reg.setRadio(regCli.getZona());
        reg.setCorrelativo_ruta(regCli.getCorrelativo_ruta());
        reg.setRol_solicitud("GLOBAL");
        reg.setSucursal_rol_solic(regCli.getSucursal());
        reg.setDir_provincia(regCli.getProvincia());
        reg.setDir_nom_provincia(regCli.getNom_provincia());
        reg.setDir_partido(regCli.getPartido());
        reg.setDir_nom_partido(regCli.getNom_partido());
        reg.setDir_comuna(regCli.getComuna());
        reg.setDir_nom_comuna(regCli.getNom_comuna());
        reg.setDir_cod_calle(regCli.getCod_calle());
        reg.setDir_nom_calle(regCli.getNom_calle());
        reg.setDir_numero(regCli.getNro_dir());
        reg.setDir_piso(regCli.getPiso_dir());
        reg.setDir_depto(regCli.getDepto_dir());
        reg.setDir_cod_postal(regCli.getCod_postal());
        reg.setDir_cod_entre(regCli.getCod_entre());
        reg.setDir_nom_entre(regCli.getNom_entre());
        reg.setDir_cod_entre1(regCli.getCod_entre1());
        reg.setDir_nom_entre1(regCli.getNom_entre1());
        reg.setDir_observacion(regCli.getObs_dir().trim());
        reg.setDir_cod_barrio(regCli.getBarrio());
        reg.setDir_nom_barrio(regCli.getNom_barrio());
        reg.setDir_manzana(regCli.getManzana());
        reg.setNombre(regCli.getNombre());
        reg.setTip_doc(regCli.getTip_doc());
        reg.setNro_doc(regCli.getNro_doc());
        reg.setTelefono(regCli.getTelefono());
        reg.setMot_denuncia(sCodMotivo.trim());
        reg.setObservacion1(sObserva.trim());
        reg.setMarca_medidor(regCli.getMarca_medidor());
        reg.setModelo_medidor(regCli.getModelo_medidor());
        reg.setNro_medidor(regCli.getNumero_medidor());

        //System.out.println("Cliente " + reg.getNumero_cliente());
        return reg;
    }

    private boolean GrabaNvaSol(InspeSolicitudDTO reg, String typeOfSelection, Connection connection )throws SQLException{
    String sLinea="";
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
            stmt.setLong(2,reg.getNumero_cliente());
            stmt.setString(3,reg.getSucursal().trim());
            stmt.setInt(4,reg.getPlan());
            stmt.setInt(5,reg.getRadio());
            stmt.setLong(6,reg.getCorrelativo_ruta());
            stmt.setString(7,reg.getRol_solicitud().trim());
            stmt.setString(8, reg.getSucursal_rol_solic().trim());
            stmt.setString(9, reg.getDir_provincia());
            stmt.setString(10, reg.getDir_nom_provincia().trim());
            stmt.setString(11, reg.getDir_partido());
            stmt.setString(12, reg.getDir_nom_partido().trim());
            stmt.setString(13, reg.getDir_comuna());
            stmt.setString(14, reg.getDir_nom_comuna().trim());
            stmt.setString(15, reg.getDir_cod_calle());
            stmt.setString(16, reg.getDir_nom_calle().trim());
            stmt.setString(17, reg.getDir_numero().trim());
            stmt.setString(18, reg.getDir_piso().trim());
            stmt.setString(19, reg.getDir_depto().trim());

            stmt.setInt(20, reg.getDir_cod_postal());
            stmt.setString(21, reg.getDir_cod_entre());
            stmt.setString(22, reg.getDir_nom_entre());
            stmt.setString(23, reg.getDir_cod_entre1());
            stmt.setString(24, reg.getDir_nom_entre1());

            stmt.setString(25, reg.getDir_observacion());
            stmt.setString(26, reg.getDir_cod_barrio());
            stmt.setString(27, reg.getDir_nom_barrio());
            stmt.setString(28, reg.getDir_manzana());
            stmt.setString(29, reg.getNombre().trim());
            stmt.setString(30, reg.getTip_doc());
            //stmt.setFloat(31, reg.getNro_doc());
            stmt.setObject(31, reg.getNro_doc(), Types.FLOAT);
            stmt.setString(32, reg.getTelefono().trim());
            stmt.setString(33, reg.getMot_denuncia());
            stmt.setString(34, reg.getObservacion1().trim());
            stmt.setString(35, reg.getMarca_medidor());
            stmt.setString(36, reg.getModelo_medidor());
            stmt.setLong(37, reg.getNro_medidor());
            stmt.setInt(38, iTipoExtractor);
            stmt.setString(39, esIndividual.trim());
            stmt.setString(40, esGrupo.trim());

            stmt.executeUpdate();
        }
        return true;
    }

    private long getNvaSolicitud(long nroCliente, String sCodMotivo, Connection connection)throws SQLException{
        long nroSol=0;

        try(PreparedStatement stmt = connection.prepareStatement(SEL_NVA_SOLICITUD)) {
            stmt.setLong(1, nroCliente);
            stmt.setString(2, sCodMotivo.trim());
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    nroSol=rs.getInt(1);
                }else{
                    nroSol = -1;
                }
            }
        }

        return nroSol;
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
            "type_of_selection, " +
            "tarifa, " +
            "cod_estado, " +
            "fecha_estado " +
            ")VALUES( " +
            "?, ?, ?, ?, ?, ?, TODAY) ";

    private static final String INS_RECHAZO = "INSERT INTO sol_inspecciones ( " +
            "id_caso, " +
            "numero_cliente, " +
            "cod_motivo, " +
            "tarifa, " +
            "cod_estado, " +
            "desc_estado, " +
            "type_of_selection, " +
            "fecha_estado " +
            ")VALUES( " +
            "?, ?, ?, ?, ?, ?, ?, TODAY) ";

    private static final String SEL_INDIV_SOL = "SELECT COUNT(*) FROM inspecc:in_solicitud " +
            "WHERE numero_cliente = ? " +
            "AND estado NOT IN (3, 7) " +
            "AND tipo_extractor IN (5, 6) ";

    private static final String SEL_CLIENTE_T1 = "SELECT c.sucursal, c.sector, c.zona, " +
            "c.correlativo_ruta, c.provincia, c.nom_provincia, " +
            "c.partido, c.nom_partido, c.comuna, " +
            "c.nom_comuna, c.cod_calle, c.nom_calle, " +
            "c.nro_dir, nvl(c.piso_dir, ' '), nvl(c.depto_dir, ' '), " +
            "c.cod_entre, c.nom_entre, c.cod_entre1, " +
            "c.nom_entre1, c.barrio, c.nom_barrio, " +
            "c.nombre, c.tip_doc, c.nro_doc, " +
            "nvl(c.telefono, ' '), c.estado_cliente, c.cod_postal, " +
            "nvl(c.obs_dir, ' '), c.manzana, " +
            "m.numero_medidor, m.marca_medidor, m.modelo_medidor, NVL(dt.dias_insp_mismo_or, 365) " +
            "FROM cliente c, OUTER medid m, inspecc:in_sucursal sp, inspecc:in_sucur_dias_tar dt " +
            "WHERE c.numero_cliente = ? " +
            "AND m.numero_cliente = c.numero_cliente " +
            "AND m.estado = 'I' " +
            "AND sp.codigo = c.sucursal " +
            "AND dt.sucursal = sp.padre " +
            "AND dt.tarifa = c.tarifa ";


    private static final String SEL_ULTIMA_SOL_T1 = "SELECT s1.nro_solicitud, s1.tipo_extractor, s1.estado,  " +
            "today - s1.fecha_estado dif_dias, " +
            "s1.sucursal_rol_solic, s1.numero_cliente, NVL(i.ejecuto_inspeccion, 'S') inspec_ejec " +
            "FROM inspecc:in_solicitud s1, OUTER inspecc:in_inspeccion i " +
            "WHERE s1.numero_cliente = ? " +
            "AND s1.fecha_estado = (SELECT MAX(s2.fecha_estado) " +
            "   FROM inspecc:in_solicitud s2 " +
            "   WHERE s2.numero_cliente = s1.numero_cliente) " +
            "AND i.nro_solicitud = s1.nro_solicitud ";


    private static final String UPD_SOL_ANEXADA_IND = "UPDATE inspecc:in_solicitud SET " +
            "es_individual = 'S', " +
            "tipo_extractor = 6, " +
            "mot_denuncia = ?, " +
            "observacion2 = nvl(observacion2, '-') || '-Anexada por ws Global', " +
            "estado = ?, " +
            "fecha_estado = TODAY " +
            "WHERE nro_solicitud = ? ";

    private static final String UPD_SOL_ANEXADA_GRP = "UPDATE inspecc:in_solicitud SET " +
            "es_grupo = 'S', " +
            "tipo_extractor = 5, " +
            "mot_denuncia = ?, " +
            "observacion2 = nvl(observacion2, '-') || '-Anexada por ws Global', " +
            "estado = ?, " +
            "fecha_estado = TODAY " +
            "WHERE nro_solicitud = ? ";

    private static final String INS_INSPE_ANEXADA = "INSERT INTO inspecc:in_inspeccion ( sucursal_rol, nro_inspeccion, " +
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

    private static final String UPD_SOL_OCURRENCIA_IND = "UPDATE inspecc:in_solicitud SET " +
            "es_individual = 'S', " +
            "observacion2 = nvl(observacion2, '-') || '-se registro ocurrencia por ws Global' " +
            "WHERE nro_solicitud = ? ";

    private static final String UPD_SOL_OCURRENCIA_GRP = "UPDATE inspecc:in_solicitud SET " +
            "es_grupo = 'S', " +
            "observacion2 = nvl(observacion2, '-') || '-se registro ocurrencia por ws Global' " +
            "WHERE nro_solicitud = ? ";

    private static final String UPD_CASO="UPDATE sol_inspecciones SET " +
            "nro_solicitud_inspeccion = ?, " +
            "cod_estado = ?, " +
            "desc_estado = ? " +
            "WHERE id_caso = ? " +
            "AND numero_cliente = ? ";

    private static final String SEL_CNR = "SELECT COUNT(*) " +
            "FROM cnr_new " +
            "WHERE numero_cliente = ? " +
            "AND cod_estado <> '99' ";

    private static final String INS_SOLICITUD = "INSERT INTO inspecc:in_solicitud (  " +
            "estado, fecha_estado, fecha_solicitud, numero_cliente, " +
            "sucursal, plan, radio, correlativo_ruta, " +
            "rol_solicitud, sucursal_rol_solic, " +
            "dir_provincia, dir_nom_provincia, dir_partido, " +
            "dir_nom_partido, dir_comuna, dir_nom_comuna, " +
            "dir_cod_calle, dir_nom_calle, dir_numero, dir_piso, " +
            "dir_depto, dir_cod_postal, dir_cod_entre, dir_nom_entre, " +
            "dir_cod_entre1, dir_nom_entre1, dir_observacion, " +
            "dir_cod_barrio, dir_nom_barrio, dir_manzana, " +
            "nombre, tip_doc, nro_doc, telefono, mot_denuncia, " +
            "observacion1, " +
            "marca_medidor, modelo_medidor, nro_medidor, tipo_extractor, es_individual, es_grupo )  " +
            "VALUES ( ? , today, today, ?, " +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
            "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    private static final String SEL_NVA_SOLICITUD = "SELECT s1.nro_solicitud FROM inspecc:in_solicitud s1 " +
            "WHERE s1.numero_cliente = ? " +
            "AND s1.mot_denuncia = ? " +
            "AND s1.fecha_estado = (SELECT MAX(s2.fecha_estado) FROM inspecc:in_solicitud s2 " +
            "   WHERE s2.numero_cliente = s1.numero_cliente " +
            "   AND s2.mot_denuncia = s1.mot_denuncia) ";
}

