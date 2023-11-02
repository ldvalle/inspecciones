package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.beans.SolicitudInspeccion;
import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import edesur.hurto.inspecciones.model.ListaMultiSolRequest;
import org.apache.camel.LoggingLevel;
import edesur.hurto.inspecciones.model.InspeSolicitudResponse;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class MultiSolRouteBuilder  extends BaseRouteBuilder{
    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:setCargaMasivaInspeccion")
                .routeId("setCargaMasivaInspeccion")
                .setBody(simple("${body.getListadoSol()}"))
                .to("bean:generarSolicitud?method=cierreMasivoResponse()")
                .log(LoggingLevel.DEBUG, logname, "Lote Procesado")
        .end();
/*
                //.wireTap("direct:RespuestaAutomatica")
                .setBody(simple("${body.getListadoSol()}"))
                .split().body().parallelProcessing(true)
                    .to("direct:hacemosAlgo")
                    //.log(LoggingLevel.DEBUG, logname, "${body.getSPID}")
                .end()
                .log(LoggingLevel.DEBUG, logname, "Lote Procesado")

        .end();

        from("direct:hacemosAlgo")
                .routeId("hacemosAlgo")
                .log(LoggingLevel.DEBUG, logname, "hicimos algo")
        .end();
*/
    }
}
