package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.InspeSolicitudResponse;
import edesur.hurto.inspecciones.model.ClienteDTO;
import edesur.hurto.inspecciones.model.InspeSolicitudDTO;

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
        SuscriptorDAO srvCli = new SuscriptorDAO();

        try(Connection conectOra = dataSourceCandela.getConnection()) {
            regCli= srvCli.getSuscriptor(nroCliente, conectOra);
            //if(ValidaCliente(nroCliente)){
            if(regCli.getEstado_cliente()==0){
                //todo

                System.out.println("Nombre " + regCli.getNombre().trim());
                System.out.println("Localidad " + regCli.getComuna());


            }else{
                //todo
                System.out.println("cliente no activo . Sts = " + regCli.getEstado_cliente());
            }
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return true;
    }


    private boolean ValidaCliente(long nroCliente) throws SQLException{
        int iEstadoCliente = -1;
        String sNombre="";

        try(Connection connection = dataSourceCandela.getConnection()) {
            try(PreparedStatement stmt = connection.prepareStatement(SEL_CLIENTE)) {
                stmt.setLong(1, nroCliente);
                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()){
                        iEstadoCliente=rs.getInt(1);
                        sNombre=rs.getString(2);

System.out.println("Nombre " + sNombre.trim());
                    }
                }
            }
        }


        return true;
    }

    private static final String SEL_CLIENTE = "SELECT estado_suscriptor, nombre from suscriptor " +
            "WHERE codigo_cuenta = ? ";

}
