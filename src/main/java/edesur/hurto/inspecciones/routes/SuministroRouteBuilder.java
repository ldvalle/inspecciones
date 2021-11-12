package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.BeanAggregationStrategy;
import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import org.apache.camel.LoggingLevel;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SuministroRouteBuilder extends BaseRouteBuilder {
    private final AggregationStrategy aggregationStrategy = new BeanAggregationStrategy();

    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:getSuministro")
                .routeId("getSuministro")
                .setHeader("numeroCliente", simple("${body.getListaResultado.get(0).getNumeroSuministro}"))
                .log(LoggingLevel.DEBUG, logname, "Consulta suministro cliente ${header.numeroCliente}")
                .to("sql:classpath:sql/suministro.sql?dataSource=#SynergiaDS&outputType=SelectOne&outputClass=edesur.hurto.inspecciones.model.SuministroResultado")
                .choice()
                    .when(body().isNull())
                        .log(LoggingLevel.DEBUG, logname, "Cliente ${header.numeroCliente} no existe")
                        .setBody(constant(ErrorResponse.create(ErrorType.ClienteNoExiste)))
                    .otherwise()
                        .log(LoggingLevel.DEBUG, logname, "${body}")
                        .log(LoggingLevel.DEBUG, logname, "SQL cortePorDeuda")
                        .enrich(sqlUri("cortePorDeuda"), aggregationStrategy )
                        .log(LoggingLevel.DEBUG, logname, "SQL fechaIngresoPago")
                        .enrich(sqlUri("fechaIngresoPago"), aggregationStrategy)
                        .log(LoggingLevel.DEBUG, logname, "SQL fechaNotificacion")
                        .enrich(sqlUri("fechaNotificacion"), aggregationStrategy)
                        .log(LoggingLevel.DEBUG, logname, "SQL fechaProximaFactura")
                        .enrich(sqlUri("fechaProximaFactura"), aggregationStrategy)
                        .log(LoggingLevel.DEBUG, logname, "SQL pagoEnProceso")
                        .enrich(sqlUri("pagoEnProceso"), aggregationStrategy)
                        .bean(SuministroResponseBuilder.class)
                .end();
    }

    private String sqlUri(String sqlfile) {
        return "sql:classpath:sql/" + sqlfile + ".sql?dataSource=#SynergiaDS&outputType=SelectOne";
    }
}
