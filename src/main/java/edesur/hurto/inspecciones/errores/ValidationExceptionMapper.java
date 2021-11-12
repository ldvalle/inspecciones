package edesur.hurto.inspecciones.errores;

import org.apache.cxf.validation.ResponseConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    private static final Logger logger = LoggerFactory.getLogger(ValidationExceptionMapper.class);

    @Override
    public Response toResponse(ValidationException exception) {
        Response response;

        logger.error("Exception {}, {}", exception.getClass().getName(), exception.getMessage());

        if (exception instanceof ConstraintViolationException) {
            Response.ResponseBuilder rb;

            final ConstraintViolationException constraint = (ConstraintViolationException) exception;

            ErrorResponse errorResponse;

            if (constraint instanceof ResponseConstraintViolationException) {
                errorResponse = ErrorResponse.createFromException(exception);
            }
            else {
                errorResponse = ErrorResponse.create(ErrorType.ParametersValidationError);
            }

            if (constraint instanceof ResponseConstraintViolationException) {
                rb = Response.serverError().entity(errorResponse); //ErrorResponse.InternalError);
            }
            else {
                rb = Response.status(Response.Status.BAD_REQUEST).entity(errorResponse); //ErrorResponse.ParametersValidationError);
            }

            int i = 0;
            for (final ConstraintViolation< ? > violation: constraint.getConstraintViolations()) {
                logger.warn(
                        "{}.{}: {}",
                        violation.getRootBeanClass().getSimpleName(),
                        violation.getPropertyPath(),
                        violation.getMessage());

                i++;
                rb.header(
                        String.format("Validation-Error-%02d", i),
                        String.format("%s %s", violation.getPropertyPath(),violation.getMessage())
                );
            }

            response = rb.build();
        }
        else {
            response = Response.serverError().entity(ErrorResponse.createFromException(exception)).build();
        }

        return response;
    }

}