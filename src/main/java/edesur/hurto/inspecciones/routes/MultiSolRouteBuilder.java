package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.beans.SolicitudInspeccion;
import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import edesur.hurto.inspecciones.model.ListaMultiSolRequest;
import edesur.hurto.inspecciones.model.MultiSolRequest;
import org.apache.camel.LoggingLevel;
import edesur.hurto.inspecciones.model.InspeSolicitudResponse;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class MultiSolRouteBuilder  extends BaseRouteBuilder{
    @Override
    public void configure() throws Exception {
        super.configure();
        from("direct:setCargaMasivaInspeccion")
                .routeId("setCargaMasivaInspeccion")
                .wireTap("direct:ProcesaLoteSolicitudes")
                //.setBody(simple("${body.getListadoSol()}"))
                .to("bean:generarSolicitud?method=cierreMasivoResponse()")
                .log(LoggingLevel.DEBUG, logname, "Lote Procesado")
        .end();

        from("direct:ProcesaLoteSolicitudes")
                .routeId("ProcesaLoteSolicitudes")
                .split(body())
                    .to("direct:procesaSolicitudIndividual")
                    //.log(LoggingLevel.DEBUG, logname, "SPID nro ${body.getSPID}")
                .end()
                .log(LoggingLevel.DEBUG, logname, "Fin hicimos algo")
        .end();

        from("direct:procesaSolicitudIndividual")
                .routeId("procesaSolicitudIndividual")
                .setHeader("idCaso", simple("${body.getIdOpportunity}"))
                .setHeader("numeroCliente", simple("${body.getSPID}"))
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


    }
}
