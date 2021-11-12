package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.errores.ExceptionHandler;
import org.apache.camel.builder.RouteBuilder;

public class BaseRouteBuilder extends RouteBuilder {
    protected String logname;

    @Override
    public void configure() throws Exception {
        logname = getContext().getName();

        onException(Exception.class)
                .handled(true)
                .bean(ExceptionHandler.class);
    }
}
