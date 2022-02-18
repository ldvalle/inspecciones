package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import org.apache.camel.LoggingLevel;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import edesur.hurto.inspecciones.model.ConsultaWOResponse;
import edesur.hurto.inspecciones.model.ConsultaWOResultado;
import edesur.hurto.inspecciones.beans.BeanPropertyAggregationStrategy;

public class ConsultaWORRouteBuilder  extends BaseRouteBuilder{
    private final AggregationStrategy listaResultado = new BeanPropertyAggregationStrategy<ConsultaWOResultado>("listaResultado");
    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:setConsultaWOevent")
                .routeId("setConsultaWOevent")

                .setHeader("spid", simple("${body.getSpid}"))

                .log(LoggingLevel.DEBUG, logname, "Consulta Gral.de ordenes cliente ${header.spid}")
                .setHeader("response", body())
                .to("bean:consultarOrdenes?method=getOrdenes(${header.spid})");

    }


}
