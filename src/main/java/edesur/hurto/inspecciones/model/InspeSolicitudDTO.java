package edesur.hurto.inspecciones.model;

import java.util.Date;

public class InspeSolicitudDTO {
    private Long    nro_solicitud;
    private int     tipo_extractor;
    private String  es_individual;
    private int     estado;
    private Date    fecha_estado;
    private Date    fecha_solicitud;
    private Long    numero_cliente;
    private String  sucursal;
    private int     plan;
    private int     radio;
    private Long    correlativo_ruta;
    private String  rol_solicitud;
    private String  sucursal_rol_solic;
    private int     tiene_cnr;
    private String  ult_proy_asig;
    private int     solsumin_pte;
    private int     manser_pte;
    private int     retcli_pte;
    private String  dir_provincia;
    private String  dir_nom_provincia;
    private String  dir_partido;
    private String  dir_nom_partido;
    private String  dir_comuna;
    private String  dir_nom_comuna;
    private String  dir_cod_calle;
    private String  dir_nom_calle;
    private String  dir_numero;
    private String  dir_piso;
    private String  dir_depto;
    private int  dir_cod_postal;
    private String  dir_cod_entre;
    private String  dir_nom_entre;
    private String  dir_cod_entre1;
    private String  dir_nom_entre1;
    private String  dir_observacion;
    private String  dir_cod_barrio;
    private String  dir_nom_barrio;
    private String  dir_manzana;
    private int     nro_ult_inspec;
    private Date    fecha_ult_inspec;
    private String  nombre;
    private String  tip_doc;
    private float   nro_doc;
    private String  telefono;
    private String  mot_denuncia;
    private String  observacion1;
    private String  observacion2;
    private String  marca_medidor;
    private String  modelo_medidor;
    private Long    nro_medidor;
    private int     difDiasEntre;
    private String  ejecuto_inspeccion;

    /* Setters */
    public void setNro_solicitud(Long nro_solicitud) {
        this.nro_solicitud = nro_solicitud;
    }

    public void setTipo_extractor(int tipo_extractor) {
        this.tipo_extractor = tipo_extractor;
    }

    public void setEs_individual(String es_individual) {
        this.es_individual = es_individual;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public void setFecha_estado(Date fecha_estado) {
        this.fecha_estado = fecha_estado;
    }

    public void setFecha_solicitud(Date fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }

    public void setNumero_cliente(Long numero_cliente) {
        this.numero_cliente = numero_cliente;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public void setPlan(int plan) {
        this.plan = plan;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public void setCorrelativo_ruta(Long correlativo_ruta) {
        this.correlativo_ruta = correlativo_ruta;
    }

    public void setRol_solicitud(String rol_solicitud) {
        this.rol_solicitud = rol_solicitud;
    }

    public void setSucursal_rol_solic(String sucursal_rol_solic) {
        this.sucursal_rol_solic = sucursal_rol_solic;
    }

    public void setTiene_cnr(int tiene_cnr) {
        this.tiene_cnr = tiene_cnr;
    }

    public void setUlt_proy_asig(String ult_proy_asig) {
        this.ult_proy_asig = ult_proy_asig;
    }

    public void setSolsumin_pte(int solsumin_pte) {
        this.solsumin_pte = solsumin_pte;
    }

    public void setManser_pte(int manser_pte) {
        this.manser_pte = manser_pte;
    }

    public void setRetcli_pte(int retcli_pte) {
        this.retcli_pte = retcli_pte;
    }

    public void setDir_provincia(String dir_provincia) {
        this.dir_provincia = dir_provincia;
    }

    public void setDir_nom_provincia(String dir_nom_provincia) {
        this.dir_nom_provincia = dir_nom_provincia;
    }

    public void setDir_partido(String dir_partido) {
        this.dir_partido = dir_partido;
    }

    public void setDir_nom_partido(String dir_nom_partido) {
        this.dir_nom_partido = dir_nom_partido;
    }

    public void setDir_comuna(String dir_comuna) {
        this.dir_comuna = dir_comuna;
    }

    public void setDir_nom_comuna(String dir_nom_comuna) {
        this.dir_nom_comuna = dir_nom_comuna;
    }

    public void setDir_cod_calle(String dir_cod_calle) {
        this.dir_cod_calle = dir_cod_calle;
    }

    public void setDir_nom_calle(String dir_nom_calle) {
        this.dir_nom_calle = dir_nom_calle;
    }

    public void setDir_numero(String dir_numero) {
        this.dir_numero = dir_numero;
    }

    public void setDir_piso(String dir_piso) {
        this.dir_piso = dir_piso;
    }

    public void setDir_depto(String dir_depto) {
        this.dir_depto = dir_depto;
    }

    public void setDir_cod_postal(int dir_cod_postal) {
        this.dir_cod_postal = dir_cod_postal;
    }

    public void setDir_cod_entre(String dir_cod_entre) {
        this.dir_cod_entre = dir_cod_entre;
    }

    public void setDir_nom_entre(String dir_nom_entre) {
        this.dir_nom_entre = dir_nom_entre;
    }

    public void setDir_cod_entre1(String dir_cod_entre1) {
        this.dir_cod_entre1 = dir_cod_entre1;
    }

    public void setDir_nom_entre1(String dir_nom_entre1) {
        this.dir_nom_entre1 = dir_nom_entre1;
    }

    public void setDir_observacion(String dir_observacion) {
        this.dir_observacion = dir_observacion;
    }

    public void setDir_cod_barrio(String dir_cod_barrio) {
        this.dir_cod_barrio = dir_cod_barrio;
    }

    public void setDir_nom_barrio(String dir_nom_barrio) {
        this.dir_nom_barrio = dir_nom_barrio;
    }

    public void setDir_manzana(String dir_manzana) {
        this.dir_manzana = dir_manzana;
    }

    public void setNro_ult_inspec(int nro_ult_inspec) {
        this.nro_ult_inspec = nro_ult_inspec;
    }

    public void setFecha_ult_inspec(Date fecha_ult_inspec) {
        this.fecha_ult_inspec = fecha_ult_inspec;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTip_doc(String tip_doc) {
        this.tip_doc = tip_doc;
    }

    public void setNro_doc(float nro_doc) {
        this.nro_doc = nro_doc;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setMot_denuncia(String mot_denuncia) {
        this.mot_denuncia = mot_denuncia;
    }

    public void setObservacion1(String observacion1) {
        this.observacion1 = observacion1;
    }

    public void setObservacion2(String observacion2) {
        this.observacion2 = observacion2;
    }

    public void setMarca_medidor(String marca_medidor) {
        this.marca_medidor = marca_medidor;
    }

    public void setModelo_medidor(String modelo_medidor) {
        this.modelo_medidor = modelo_medidor;
    }

    public void setNro_medidor(Long nro_medidor) {
        this.nro_medidor = nro_medidor;
    }

    public void setDifDiasEntre(int difDiasEntre) {
        this.difDiasEntre = difDiasEntre;
    }

    public void setEjecuto_inspeccion(String ejecuto_inspeccion){this.ejecuto_inspeccion = ejecuto_inspeccion; }

    /* Getters */
    public Long getNro_solicitud() {
        return nro_solicitud;
    }

    public int getTipo_extractor() {
        return tipo_extractor;
    }

    public String getEs_individual() {
        return es_individual;
    }

    public int getEstado() {
        return estado;
    }

    public Date getFecha_estado() {
        return fecha_estado;
    }

    public Date getFecha_solicitud() {
        return fecha_solicitud;
    }

    public Long getNumero_cliente() {
        return numero_cliente;
    }

    public String getSucursal() {
        return sucursal;
    }

    public int getPlan() {
        return plan;
    }

    public int getRadio() {
        return radio;
    }

    public Long getCorrelativo_ruta() {
        return correlativo_ruta;
    }

    public String getRol_solicitud() {
        return rol_solicitud;
    }

    public String getSucursal_rol_solic() {
        return sucursal_rol_solic;
    }

    public int getTiene_cnr() {
        return tiene_cnr;
    }

    public String getUlt_proy_asig() {
        return ult_proy_asig;
    }

    public int getSolsumin_pte() {
        return solsumin_pte;
    }

    public int getManser_pte() {
        return manser_pte;
    }

    public int getRetcli_pte() {
        return retcli_pte;
    }

    public String getDir_provincia() {
        return dir_provincia;
    }

    public String getDir_nom_provincia() {
        return dir_nom_provincia;
    }

    public String getDir_partido() {
        return dir_partido;
    }

    public String getDir_nom_partido() {
        return dir_nom_partido;
    }

    public String getDir_comuna() {
        return dir_comuna;
    }

    public String getDir_nom_comuna() {
        return dir_nom_comuna;
    }

    public String getDir_cod_calle() {
        return dir_cod_calle;
    }

    public String getDir_nom_calle() {
        return dir_nom_calle;
    }

    public String getDir_numero() {
        return dir_numero;
    }

    public String getDir_piso() {
        return dir_piso;
    }

    public String getDir_depto() {
        return dir_depto;
    }

    public int getDir_cod_postal() {
        return dir_cod_postal;
    }

    public String getDir_cod_entre() {
        return dir_cod_entre;
    }

    public String getDir_nom_entre() {
        return dir_nom_entre;
    }

    public String getDir_cod_entre1() {
        return dir_cod_entre1;
    }

    public String getDir_nom_entre1() {
        return dir_nom_entre1;
    }

    public String getDir_observacion() {
        return dir_observacion;
    }

    public String getDir_cod_barrio() {
        return dir_cod_barrio;
    }

    public String getDir_nom_barrio() {
        return dir_nom_barrio;
    }

    public String getDir_manzana() {
        return dir_manzana;
    }

    public int getNro_ult_inspec() {
        return nro_ult_inspec;
    }

    public Date getFecha_ult_inspec() {
        return fecha_ult_inspec;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTip_doc() {
        return tip_doc;
    }

    public float getNro_doc() {
        return nro_doc;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getMot_denuncia() {
        return mot_denuncia;
    }

    public String getObservacion1() {
        return observacion1;
    }

    public String getObservacion2() {
        return observacion2;
    }

    public String getMarca_medidor() {
        return marca_medidor;
    }

    public String getModelo_medidor() {
        return modelo_medidor;
    }

    public Long getNro_medidor() {
        return nro_medidor;
    }

    public int getDifDiasEntre() {
        return difDiasEntre;
    }

    public String getEjecuto_inspeccion(){return ejecuto_inspeccion; }
}
