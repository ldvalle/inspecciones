package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import org.apache.camel.LoggingLevel;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import edesur.hurto.inspecciones.model.InspeHistoResponse;
import edesur.hurto.inspecciones.model.InspeHistoResultado;
import edesur.hurto.inspecciones.beans.BeanPropertyAggregationStrategy;

public class ConsultaHistoInspeRouteBuilder extends BaseRouteBuilder{
    private final AggregationStrategy listaResultado = new BeanPropertyAggregationStrategy<InspeHistoResultado>("listaResultado");
    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:setConsultaHistoInspeccion")
                .routeId("setConsultaHistoInspeccion")

                .setHeader("numeroCliente", simple("${body.getNumeroCliente}"))

                .log(LoggingLevel.DEBUG, logname, "Consulta Historia Inspecciones Cliente ${header.numeroCliente}")
                .setHeader("response", body())
                .to("bean:consultarHistoInspecciones?method=getHistoInspe(${header.numeroCliente})");

    }

}
