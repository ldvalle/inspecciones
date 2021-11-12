package edesur.hurto.inspecciones.beans;

import edesur.hurto.inspecciones.model.ClienteDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

public class ClienteDAO {
    public DataSource dataSource;

    public void setDataSource(DataSource dataSource) { this.dataSource = dataSource; }

    public ClienteDTO getCliente(Long numero_cliente) {
        ClienteDTO reg = new ClienteDTO();
        reg.setNumero_cliente(numero_cliente);

        try(Connection connection = dataSource.getConnection()) {
            try(PreparedStatement stmt = connection.prepareStatement(SEL_CLIENTE)) {
                stmt.setLong(1, numero_cliente);
                try(ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()){
                        reg.setSucursal(rs.getString(1));
                        reg.setSector(rs.getInt(2));
                        reg.setZona(rs.getInt(3));
                        reg.setCorrelativo_ruta(rs.getLong(4));
/*
                        this.provincia=rs.getString(5);
                        this.nom_provincia=rs.getString(6);
                        this.partido=rs.getString(7);
                        this.nom_partido=rs.getString(8);
                        this.comuna=rs.getString(9);
                        this.nom_comuna=rs.getString(10);
                        this.cod_calle=rs.getString(11);
                        this.nom_calle=rs.getString(12);
                        this.nro_dir=rs.getString(13);
                        this.piso_dir=rs.getString(14);
                        this.depto_dir=rs.getString(15);
                        this.cod_entre=rs.getString(16);
                        this.nom_entre=rs.getString(17);
                        this.cod_entre1=rs.getString(18);
                        this.nom_entre1=rs.getString(19);
                        this.barrio=rs.getString(20);
                        this.nom_barrio=rs.getString(21);
                        this.nombre=rs.getString(22);
                        this.tip_doc=rs.getString(23);
                        this.nro_doc=rs.getFloat(24);
                        this.telefono=rs.getString(25);
                        this.estado_cliente=rs.getInt(26);
                        this.cod_postal=rs.getInt(27);
                        this.obs_dir=rs.getString(28);
                        this.manzana=rs.getString(29);
*/
                    }else{
                        reg.setEstado_cliente(-1);
                    }
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        return reg;
    }

    private static final String SEL_CLIENTE = "SELECT sucursal, sector, zona, " +
            "correlativo_ruta, provincia, nom_provincia, " +
            "partido, nom_partido, comuna, " +
            "nom_comuna, cod_calle, nom_calle, " +
            "nro_dir, piso_dir, depto_dir, " +
            "cod_entre, nom_entre, cod_entre1, " +
            "nom_entre1, barrio, nom_barrio, " +
            "nombre, tip_doc, nro_doc, " +
            "telefono, estado_cliente, cod_postal, " +
            "obs_dir, manzana " +
            "FROM cliente " +
            "WHERE numero_cliente = ? ";

}
