package edesur.hurto.inspecciones.errores;

public enum ErrorType {
    ParametersValidationError("E400SC0000", "Error en validación de parámetros"),
    ClienteNoExiste ("E400SC0001", "Cliente Inexistente"),
    ClienteNoEnEstado("E400SC0002", "Cliente Inexistente, Inactivo o en Ciclo de Facturación"),

    //InternalError("E500SC0000", "Error Interno"),
    InternalError("KO", "Error Interno"),
    DeudaNoExiste("E600SC0001", "Deuda no existe"),
    //SqlError("E500SC0001", "Error SQL");
    SqlError("KO", "Error SQL");
    

    private String codigo_retorno;
    private String descripcion_retorno;

    private ErrorType(String codigo_retorno, String descripcion_retorno) {
        this.codigo_retorno = codigo_retorno;
        this.descripcion_retorno = descripcion_retorno;
    }

    public String getCodigo_retorno() {
        return codigo_retorno;
    }

    public String getDescripcion_retorno() {
        return descripcion_retorno;
    }

}
