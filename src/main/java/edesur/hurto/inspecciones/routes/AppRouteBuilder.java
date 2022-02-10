package edesur.hurto.inspecciones.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.component.cxf.common.message.CxfConstants;

public class AppRouteBuilder extends BaseRouteBuilder {
    @Override
    public void configure() throws Exception {
        super.configure();

        from("cxfrs:bean:rsServer?bindingStyle=SimpleConsumer")
                .routeId("restEndpointConsumer")
                .choice()
                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("SolicitudInspeccion"))
                        .to("direct:setSolicitudInspeccion")

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("ConsultaInspeccion"))
                        .to("direct:setConsultaInspeccion")

                    .when(header(CxfConstants.OPERATION_NAME).isEqualTo("ConsultaInspeccion2"))
                        .to("direct:setConsultaInspeccion2")

                    .otherwise()
                        .log(LoggingLevel.ERROR, "Operacion no implementada: " + header(CxfConstants.OPERATION_NAME))
                .end();
    }
}
