package edesur.hurto.inspecciones.routes;


import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import org.apache.camel.LoggingLevel;
import edesur.hurto.inspecciones.model.InspeStatusResponse;

public class ConsultaInspeRouteBuilder extends BaseRouteBuilder{
    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:setConsultaInspeccion")
                .routeId("setConsultaInspeccion")

                .setHeader("idCaso", simple("${body.getIdCaso}"))
                .setHeader("numeroCliente", simple("${body.getNumeroCliente}"))

                .log(LoggingLevel.DEBUG, logname, "Consulta Estado Inspeccion para caso ${header.idCaso}")
                .setHeader("response", body())
                .to("bean:consultarSolicitud?method=getConsultaInspe(${header.idCaso}, ${header.numeroCliente})");

    }

}
