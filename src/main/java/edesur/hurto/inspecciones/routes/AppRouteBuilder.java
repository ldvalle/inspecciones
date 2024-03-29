package edesur.hurto.inspecciones.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.component.cxf.common.message.CxfConstants;

import static edesur.hurto.inspecciones.routes.CamelStrings.stringWithHeaders;

public class AppRouteBuilder extends BaseRouteBuilder {
    @Override
    public void configure() throws Exception {
        super.configure();

        from("cxfrs:bean:rsServer?bindingStyle=SimpleConsumer")
                .routeId("restEndpointConsumer")
                .choice()
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("SolicitudInspeccion"))
                        .to("direct:setSolicitudInspeccion")

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("SolicitudMultiInspeccion"))
                        .to("direct:setMultiSolInspeccion")

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("SolicitudMasiva"))
                        .to("direct:setCargaMasivaInspeccion")

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("ConsultaInspeccion"))
                        .to("direct:setConsultaInspeccion")

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("ConsultaInspeccion2"))
                        .to("direct:setConsultaInspeccion2")

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("getWorkOrderID"))
                        .to("direct:setConsultaHistoInspeccion")

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("getEventWO"))
                        .to("direct:setConsultaWOevent")
                    .otherwise()
                        .log(LoggingLevel.ERROR, stringWithHeaders("Operacion no implementada:%s",CxfConstants.OPERATION_NAME))
                .end();
    }
}
