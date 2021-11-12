package edesur.hurto.inspecciones.errores;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edesur.hurto.inspecciones.model.ResponseBase;
import org.apache.camel.component.bean.validator.BeanValidationException;

import javax.ws.rs.core.Response;
import java.sql.SQLException;

public final class ErrorResponse extends ResponseBase {
    @JsonIgnore
    private Response.Status statusCode;

    public static ErrorResponse create(ErrorType type) {
        return new ErrorResponse(type.getCodigo_retorno(), type.getDescripcion_retorno());
    }

    public static ErrorResponse create(ErrorType type, String descripcion_retorno) {
        return new ErrorResponse(type.getCodigo_retorno(), descripcion_retorno);
    }

    public static ErrorResponse createFromException(Exception e) {
        ErrorResponse errorResponse;
        if (e instanceof SQLException) {
            errorResponse = create(ErrorType.SqlError);
            errorResponse.statusCode = Response.Status.INTERNAL_SERVER_ERROR;
            errorResponse.setDescripcion_retorno(e.getMessage());
        }
        if (e instanceof BeanValidationException) {
            errorResponse = new ErrorResponse((BeanValidationException)e);
        }
        else {
            errorResponse = create(ErrorType.InternalError);
            errorResponse.statusCode = Response.Status.INTERNAL_SERVER_ERROR;
            errorResponse.setDescripcion_retorno(e.getMessage());
        }
        return errorResponse;
    }

    private ErrorResponse(String codigo_retorno, String descripcion_retorno) {
        super.setCodigo_retorno((codigo_retorno));
        super.setDescripcion_retorno((descripcion_retorno));
    }

    private ErrorResponse(BeanValidationException e) {
        super.setCodigo_retorno(ErrorType.ParametersValidationError.getCodigo_retorno());
        String m = e.getMessage().replaceAll("(?s)^(.*)( for: .*)(errors: \\[.*\\])(.*)$", "$1, $3");
        super.setDescripcion_retorno(m);
        statusCode = Response.Status.BAD_REQUEST;
    }

    public int getStatusCode() {
        return statusCode.getStatusCode();
    }
}
