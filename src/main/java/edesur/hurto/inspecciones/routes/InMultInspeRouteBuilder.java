package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.beans.SolicitudInspeccion;
import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import org.apache.camel.LoggingLevel;
import edesur.hurto.inspecciones.model.InspeSolicitudResponse;

public class InMultInspeRouteBuilder extends BaseRouteBuilder {
    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:setMultiSolInspeccion")
                .routeId("setMultiSolInspeccion")
                .wireTap("direct:RespuestaAutomatica")
                .setBody(simple("${body.getListaSol()}"))
                .split().body().parallelProcessing(true)
                    .to("direct:procesaUnaSolicitud")
                    //.log(LoggingLevel.DEBUG, logname, "${body.getNumeroCliente}")
                .end()
                .log(LoggingLevel.DEBUG, logname, "Lote Procesado")

        .end();

        from("direct:procesaUnaSolicitud")
                .routeId("procesaUnaSolicitud")
                    .setHeader("idCaso", simple("${body.getIdCaso}"))
                    .setHeader("numeroCliente", simple("${body.getNumeroCliente}"))
                    .setHeader("typeOfSelection", simple("${body.getTypeOfSelection}"))
                    .log(LoggingLevel.DEBUG, logname, "Caso ${header.idCaso} Sol Inspeccion para cliente ${header.numeroCliente}")
                    .setHeader("response", body())
                    .transacted()

                    .choice()
                        .when(header("numeroCliente").isLessThan(80000000))
                        .to("bean:generarSolicitud?method=CreateSolicitud(${header.idCaso}, ${header.numeroCliente}, ${header.typeOfSelection})")
                    .otherwise()
                        .to("bean:generarSolicitudT23?method=CreateSolicitud(${header.idCaso}, ${header.numeroCliente}, ${header.typeOfSelection})")
                    .end()
        .end();

        from("direct:RespuestaAutomatica")
                .log(LoggingLevel.DEBUG, logname, "Entra a la automatica")
                .to("bean:generarSolicitud?method=cierreMasivoResponse()")
        .end();
    }
}

