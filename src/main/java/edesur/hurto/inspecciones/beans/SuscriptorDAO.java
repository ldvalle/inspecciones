package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.ClienteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SuscriptorDAO {

    public ClienteDTO getSuscriptor(long nroCliente, Connection connection) throws SQLException{
        ClienteDTO reg = new ClienteDTO();
        String nroDocumento="";
        long auxNroCliente=0;

        if(nroCliente > 80000000 && nroCliente < 80500000) {
            auxNroCliente = nroCliente - 80000000;
        }else{
            auxNroCliente = nroCliente;
        }

        try(PreparedStatement stmt = connection.prepareStatement(SEL_SUSCRIPTOR)) {
            //stmt.setLong(1, nroCliente);
            stmt.setLong(1, auxNroCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reg.setNumero_cliente(rs.getLong(1));
                    reg.setEstado_cliente(rs.getInt(2));
                    reg.setSucursal(rs.getString(3));
                    reg.setSector(rs.getInt(4));
                    reg.setCorrelativo_ruta(rs.getLong(5));
                    reg.setPartido(rs.getString(6));
                    reg.setNom_comuna(rs.getString(7));
                    reg.setCod_calle(rs.getString(8));
                    reg.setNom_calle(rs.getString(9));
                    reg.setNro_dir(rs.getString(10));
                    reg.setPiso_dir(rs.getString(11));
                    reg.setDepto_dir(rs.getString(12));
                    reg.setCod_postal(rs.getInt(13));
                    reg.setCod_entre(rs.getString(14));
                    reg.setNom_entre(rs.getString(15));
                    reg.setCod_entre1(rs.getString(16));
                    reg.setNom_entre1(rs.getString(17));
                    reg.setManzana(rs.getString(18));
                    reg.setNombre(rs.getString(19));
                    reg.setEstadoSuministro(rs.getInt(20));
                    reg.setTip_doc(rs.getString(21));
                    //nroDocumento=rs.getString(22);
                    reg.setNro_doc(Float.parseFloat(rs.getString(22)));
                    reg.setTelefono(rs.getString(23));
                    reg.setTipoTarifaT23(rs.getInt(24));

                    if(reg.getNom_comuna().trim().equals("CAPITAL FEDERAL")){
                        reg.setProvincia("C");
                        reg.setNom_provincia("CAPITAL FEDERAL");
                    }else{
                        reg.setProvincia("B");
                        reg.setNom_provincia("BUENOS AIRES");
                    }
                }else{
                    reg.setEstado_cliente(-1);
                }
            }
        }

        try(PreparedStatement stmt = connection.prepareStatement(SEL_QUERELLA)) {
            //stmt.setLong(1, nroCliente);
            stmt.setLong(1, auxNroCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reg.setTieneQuerella(rs.getInt(1));
                }else{
                    reg.setTieneQuerella(-1);
                }
            }
        }

        try(PreparedStatement stmt = connection.prepareStatement(SEL_CNR)) {
            //stmt.setLong(1, nroCliente);
            stmt.setLong(1, auxNroCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reg.setTieneCNR(rs.getInt(1));
                }else{
                    reg.setTieneCNR(-1);
                }
            }
        }

        try(PreparedStatement stmt = connection.prepareStatement(SEL_ACCIONES)) {
            stmt.setInt(1, reg.getEstadoSuministro());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reg.setAccionesTomadas(rs.getInt(1));
                }else{
                    reg.setTieneCNR(-1);
                }
            }
        }

        return reg;
    }


    private static final String SEL_SUSCRIPTOR = "SELECT  s.codigo_cuenta, s.estado_suscriptor, " +
            "SUBSTR (LPAD (TO_CHAR (s.ruta_lectura),11,0),1, 2 ) || SUBSTR (LPAD (TO_CHAR (s.ruta_lectura),11,0),3, 2 ) sucu, " +
            "s.ciclo, s.ruta_lectura, SUBSTR (LPAD (TO_CHAR (s.ruta_lectura),11,0),3, 2 ) partido, nvl(s.localidad, ' '), " +
            "p.codigo_calle, s.dir_calle, s.dir_numero, nvl(s.dir_piso, '-'), nvl(s.dir_departamento, '-'), " +
            "p.codigo_postal, p.codigo_entre1, s.dir_interseccion, p.codigo_entre2, s.dir_interseccion2, " +
            "p.codigo_manzana, s.nombre, estado_suministro, " +
            "s.tipo_identificacion,  to_char(nvl(s.nro_identificacion, 0)) || nvl(s.dv_identificacion, 0) nro_iden, " +
            "s.telefono, s.tipo_cliente  " +
            "FROM zonas z , suscriptor s, postal p " +
            "WHERE s.codigo_cuenta = ? " +
            "AND z.zona (+)= TO_NUMBER (SUBSTR (LPAD (TO_CHAR (s.ruta_lectura),11,0),1, 2 ) ) " +
            "AND z.municipio (+)= TO_NUMBER (SUBSTR (LPAD (TO_CHAR (s.ruta_lectura),11,0),3, 2 ) ) " +
            "AND p.codigo_cuenta (+)= s.codigo_cuenta ";

    private static final String SEL_QUERELLA = "SELECT COUNT(*) " +
            "FROM suscriptor2 s, tabla t " +
            "WHERE s.codigo_cuenta= ? " +
            "AND t.nomtabla='JUDIC' " +
            "AND s.estado_cob=t.codtabla " +
            "AND t.codtabla=1 ";

    private static final String SEL_CNR = "SELECT codigo_cuentacnr " +
            "FROM suscriptor2  " +
            "WHERE codigo_cuenta= ? ";

    private static final String SEL_ACCIONES = "SELECT COUNT(*) FROM tabla " +
            "WHERE nomtabla = 'ESSUM' " +
            "AND codtabla= ? ";

}
