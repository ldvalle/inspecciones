package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeSolicitudResponse;
import edesur.hurto.inspecciones.model.ClienteDTO;
import edesur.hurto.inspecciones.model.InspeSolicitudDTO;

import javax.sql.DataSource;
import javax.xml.ws.spi.ServiceDelegate;
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
        if(!ProcesoT23(idCaso, nroCliente, sCodMotivo)){
            regRes.setCodigo_retorno("1");
            regRes.setDescripcion_retorno("Fallo carga de caso " + idCaso);
        }
        regRes.setCodigo_retorno("0");
        regRes.setDescripcion_retorno("Cliente T23 - TODO ");

        return regRes;
    }

    private boolean ProcesoT23(long idCaso, long nroCliente, String sCodMotivo){
        ClienteDTO regCli = new ClienteDTO();
        InspeSolicitudDTO regUltiSol = new InspeSolicitudDTO();
        InspeSolicitudDTO regNvaSol = new InspeSolicitudDTO();
        SuscriptorDAO srvCli = new SuscriptorDAO();
        SolicitudT23DAO srvSol = new SolicitudT23DAO();
        long lNroNvaSolicitud=0;

        int iEstado=0;
        String sDescripcion="";

        try(Connection conectOra = dataSourceCandela.getConnection()) {
            regCli= srvCli.getSuscriptor(nroCliente, conectOra);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        if(regCli.getEstado_cliente()==0){
            try(Connection conectSyn = dataSourceSynergia.getConnection()) {
                //Validar Motivo
                if(!validaMotivo(sCodMotivo, conectSyn)){
                    iEstado=12;
                    sDescripcion="Codigo de Motivo inválido.";
                    if(!InsertaCaso(idCaso,nroCliente,sCodMotivo,regCli.getTipoTarifaT23(), iEstado, sDescripcion, 0, conectSyn)){
                        return false;
                    }
                    return true;
                }
                //Verifica no tener individual pendiente
                if(TieneIndividualPte(nroCliente, conectSyn)){
                    iEstado=14;
                    sDescripcion="Tiene inspeccion Individual Pendiente.";
                    if(!InsertaCaso(idCaso,nroCliente,sCodMotivo,regCli.getTipoTarifaT23(), iEstado, sDescripcion, 0, conectSyn)){
                        return false;
                    }
                    return true;
                }
                conectSyn.setAutoCommit(false);
                //Levantar la ultima solicitud
                regUltiSol=srvSol.getUltimaSolicitud(nroCliente, conectSyn);

                if(regUltiSol.getNro_solicitud()>0){
                    if(regUltiSol.getTipo_extractor()!=6 && regUltiSol.getEstado()==1){
                        //Se anexa a la individual
                        if (!srvSol.AnexaInspeccion(nroCliente, sCodMotivo, regUltiSol, iEstado, conectSyn)) {
                            return false;
                        }
                        iEstado=2;
                        sDescripcion="Se anexó a solicitud masiva solicitada.";
                        lNroNvaSolicitud=regUltiSol.getNro_solicitud();
                    }
                    iEstado=regUltiSol.getEstado();
                    if(iEstado != 1 && iEstado !=3 && iEstado !=7){
                        //Se registra la ocurrencia
                        if(! srvSol.RegistraOcurrencia(regUltiSol.getNro_solicitud(),conectSyn)){
                            return false;
                        }
                        sDescripcion = "Se registra ocurrencia con ultima solicitud pendiente.";
                        lNroNvaSolicitud=regUltiSol.getNro_solicitud();
                    }
                }else{
                    // No tiene solicitudes anteriores
                    iEstado=1;
                    sDescripcion="Inspeccion Solicitada";
                }

                if(iEstado==1 || iEstado==8){
                    //Generar la nueva solicitud
                    regCli.setSucursal_padreT23(srvSol.getSucursalPadre(regCli.getSucursal(), regCli.getTipoTarifaT23(), conectSyn));

                    regNvaSol=CargaNvaSolicitud(regCli, regUltiSol, sCodMotivo, iEstado);

                    //Grabarla

                    //Recuperar nro.de solicitud
                }

                //Insertar estado del caso

                conectSyn.commit();
            }catch (Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }



            System.out.println("Nombre " + regCli.getNombre().trim());
            System.out.println("Localidad " + regCli.getComuna());


        }else{
            switch (regCli.getEstado_cliente()){
                case -1:
                    iEstado=11;
                    sDescripcion="Cliente no Existe.";
                    break;
                default:
                    iEstado=13;
                    sDescripcion="Cliente NO Activo.";
            }

            try(Connection conectSyn = dataSourceSynergia.getConnection()) {
                if(!InsertaCaso(idCaso,nroCliente,sCodMotivo,regCli.getTipoTarifaT23(), iEstado, sDescripcion, 0, conectSyn)){
                    return false;
                }
            }catch (Exception ex){
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

        return true;
    }

    private boolean InsertaCaso(long idCaso, long nroCliente, String sCodMotivo, int tarifa, int iEstado, String sDescripcion, long nroSolicitud, Connection connection)throws  SQLException{

        try(PreparedStatement stmt = connection.prepareStatement(INS_PEDIDO)) {
            stmt.setLong(1, idCaso);
            stmt.setLong(2, nroCliente);
            stmt.setString(3, sCodMotivo.trim());
            stmt.setInt(4, tarifa);
            stmt.setInt(5, iEstado);
            stmt.setString(6, sDescripcion.trim());
            stmt.setLong(7, nroSolicitud);

            stmt.executeUpdate();

        }

        return true;
    }

    private boolean validaMotivo(String sCodMotivo, Connection connection) throws SQLException{
        int iCant=0;

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

        if(iCant <= 0){
            return false;
        }

        return true;
    }

    private boolean TieneIndividualPte(long nroCliente, Connection connection)throws SQLException{
        int iCant=0;

        try(PreparedStatement stmt = connection.prepareStatement(SEL_INDIV_SOL)) {
            stmt.setLong(1,nroCliente);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    iCant=rs.getInt(1);
                }else{
                    iCant=-1;
                    return false;
                }
            }
        }

        if(iCant <= 0){
            return false;
        }
        return true;
    }

    private InspeSolicitudDTO CargaNvaSolicitud(ClienteDTO regCli, InspeSolicitudDTO regUltiSol, String sMotDenuncia, int iEstado){
        InspeSolicitudDTO reg=new InspeSolicitudDTO();
        String sObservaciones="";

        if(regCli.getTieneCNR()>0)
            sObservaciones+= "Tiene CNR.";

        if(regCli.getTieneQuerella()>0)
            sObservaciones+= "Tiene Querella.";

        if(regCli.getAccionesTomadas()>0)
            sObservaciones+= "Tiene Acciones Tomadas.";

        reg.setNumero_cliente(regCli.getNumero_cliente());
        reg.setEstado(iEstado);
        reg.setSucursal(regCli.getSucursal_padreT23());
        reg.setPlan(regCli.getSector());
        reg.setCorrelativo_ruta(regCli.getCorrelativo_ruta());;
        reg.setRol_solicitud("GLOBAL");
        reg.setSucursal_rol_solic(regCli.getSucursal_padreT23());
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
        reg.setDir_observacion(regCli.getObs_dir());
        reg.setDir_cod_barrio(regCli.getBarrio());
        reg.setDir_nom_barrio(regCli.getNom_barrio());
        reg.setDir_manzana(regCli.getManzana());
        reg.setNro_ult_inspec(regUltiSol.getNro_solicitud());
        reg.setFecha_ult_inspec(regUltiSol.getFecha_ult_inspec());
        reg.setNombre(regCli.getNombre());
        reg.setTip_doc(regCli.getTip_doc());
        reg.setNro_doc(regCli.getNro_doc());
        reg.setTelefono(regCli.getTelefono());
        reg.setMot_denuncia(sMotDenuncia);
        reg.setObservacion1(sObservaciones);
        reg.setTipoTarifaT23(regCli.getTipoTarifaT23());
        reg.setSucursalClienteT23(regCli.getSucursal());

        return reg;
    }

    private static final String SEL_CLIENTE = "SELECT estado_suscriptor, nombre from suscriptor " +
            "WHERE codigo_cuenta = ? ";

    private static final String INS_PEDIDO = "INSERT INTO sol_inspecciones ( " +
            "id_caso, " +
            "numero_cliente, " +
            "cod_motivo, " +
            "tarifa, " +
            "cod_estado, " +
            "desc_estado " +
            "nro_solicitud_inspeccion, " +
            "fecha_estado " +
            ")VALUES( " +
            "?, ?, ?, ?, ?, ?, TODAY) ";

    private static final String SEL_VALIDA_MOTIVO="SELECT COUNT(*) FROM tabla " +
            "WHERE nomtabla = 'MOTHUR' " +
            "AND codigo = ? " +
            "AND sucursal ='0000' " +
            "AND fecha_activacion <= TODAY " +
            "AND (fecha_desactivac IS NULL OR fecha_desactivac > TODAY) ";

    private static final String SEL_INDIV_SOL = "SELECT COUNT(*) FROM inspect23:i3_solicitud " +
            "WHERE numero_cliente = ? " +
            "AND estado NOT IN (3, 7) " +
            "AND tipo_extractor = 6 ";


}
