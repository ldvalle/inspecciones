package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import org.apache.camel.LoggingLevel;
import edesur.hurto.inspecciones.model.InspeSolicitudResponse;

public class InInspeRouteBuilder extends BaseRouteBuilder {
    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:setSolicitudInspeccion")
                .routeId("setSolicitudInspeccion")

                .setHeader("idCaso", simple("${body.getIdCaso}"))
                .setHeader("numeroCliente", simple("${body.getNumeroCliente}"))
                .setHeader("codMotivo", simple("${body.getCodMotivo}"))

                .log(LoggingLevel.DEBUG, logname, "Solicitud Inspeccion para cliente ${header.numero_cliente}")
                .setHeader("response", body())
                .transacted()
                //.to("bean:generarSolicitud?method=CreateSolicitud(${header.idCaso}, ${header.numeroCliente}, ${header.codMotivo})&outputClass=edesur.hurto.inspecciones.model.InspeSolicitudResponse")
                .to("bean:generarSolicitud?method=CreateSolicitud(${header.idCaso}, ${header.numeroCliente}, ${header.codMotivo})")
                .choice()
                    .when().simple("${body.codigo_retorno} == '0'")
                        .log(LoggingLevel.DEBUG, logname, "Solicitud Ingresada")
                    .otherwise()
                        .log(LoggingLevel.DEBUG, logname, "Solicitud No Ingresada")
                .end();

        }
/*
                .to("sql:classpath:sql/rddNotiPago.sql?dataSource=#SynergiaDS&outputType=SelectOne&outputClass=edesur.rdd.srv.model.RddNotiResponse")
                .choice()
                    .when().simple("${body.codigo_retorno} == '000'")
                        .log(LoggingLevel.DEBUG, logname, "Pago Imputado")
                    .otherwise()
                        .log(LoggingLevel.DEBUG, logname, "Notificacion Abortada")


                .end();
*/

}
