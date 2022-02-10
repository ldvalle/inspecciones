package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import org.apache.camel.LoggingLevel;
import edesur.hurto.inspecciones.model.InspeStatusResponse;

public class ConsultaInspe2RouteBuilder extends BaseRouteBuilder{

    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:setConsultaInspeccion2")
                .routeId("setConsultaInspeccion2")

                .setHeader("nroSolicitud", simple("${body.getNroSolicitud}"))
                .setHeader("numeroCliente", simple("${body.getNumeroCliente}"))

                .log(LoggingLevel.DEBUG, logname, "Consulta Estado Inspeccion para solicitud ${header.nroSolicitud} cliente ${header.numeroCliente}")
                .setHeader("response", body())
                .to("bean:consultarSolicitud?method=getConsultaInspecCliente(${header.nroSolicitud}, ${header.numeroCliente})");

    }

}
