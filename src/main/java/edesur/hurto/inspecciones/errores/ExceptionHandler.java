package edesur.hurto.inspecciones.errores;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public ErrorResponse process(
            @ExchangeProperty(Exchange.EXCEPTION_CAUGHT) Exception e,
            @Headers Map<String, Object> headers) {

        logger.error(e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.createFromException(e);

        headers.put(Exchange.HTTP_RESPONSE_CODE, errorResponse.getStatusCode());

        return errorResponse;
    }
}
