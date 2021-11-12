package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import edesur.hurto.inspecciones.model.ConsultaCorteRepoResponse;
import edesur.hurto.inspecciones.model.CorteRepoResultado;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import edesur.hurto.inspecciones.beans.BeanPropertyAggregationStrategy;
//import edesur.t1.srv.beans.CorteRepoResponseBuilder;
import org.apache.camel.LoggingLevel;

public class CorteRepoRouteBuilder extends  BaseRouteBuilder {
	private final AggregationStrategy listaResultado = new BeanPropertyAggregationStrategy<CorteRepoResultado>("listaResultado");	
	
    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:setConsultaCorteRepo")
			 .routeId("setConsultaCorteRepo")
			 .setHeader("numeroSuministro", simple("${body.getListaResultado.get(0).getNumeroSuministro}"))
			 .setHeader("fechaDesde", simple("${body.getListaResultado.get(0).getFechaDesde}"))
			 .setHeader("fechaHasta", simple("${body.getListaResultado.get(0).getFechaHasta}"))
          .log(LoggingLevel.DEBUG, logname, "Consulta de Cortes y Reposiciones Cliente ${header.numeroSuministro}")
          .setHeader("response", body())
          //.to("sql:classpath:sql/ConsultaCorteRepo.sql?dataSource=#SynergiaDS&outputType=SelectList&outputClass=edesur.t1.srv.model.ConsultaCorteRepoResponse")
			 .to("sql:classpath:sql/ExisteCliente.sql?dataSource=#SynergiaDS&outputType=SelectOne&outputClass=edesur.hurto.inspecciones.model.ConsultaCorteRepoResponse")
			 //.to("sql:classpath:sql/ConsultaCorteRepo.sql?dataSource=#SynergiaDS&outputType=SelectList&outputClass=edesur.t1.srv.model.CorteRepoResultado")
			 .choice()
			 .when(body().isNull())
				  .log(LoggingLevel.DEBUG, logname, "Cliente ${header.numeroSuministro} no existe")
				  .setBody(constant(ErrorResponse.create(ErrorType.ClienteNoExiste)))
			 .otherwise()
				.enrich("sql:classpath:sql/ConsultaCorteRepo.sql?dataSource=#SynergiaDS&outputType=SelectList&outputClass=edesur.hurto.inspecciones.model.CorteRepoResultado", listaResultado )
				//.enrich(CamelUtil.sqlUriSelectOne("ConsultaCorteRepo", Configuracion.SYNERGIA_DS), listaResultado )
				//.to("sql:classpath:sql/ConsultaCorteRepo.sql?dataSource=#SynergiaDS&outputType=SelectList&outputClass=edesur.t1.srv.model.CorteRepoResultado")
				//.to("sql:classpath:sql/ExisteCliente.sql?dataSource=#SynergiaDS&outputType=SelectOne&outputClass=edesur.t1.srv.model.ConsultaCorteRepoResponse")
				.log(LoggingLevel.DEBUG, logname, "Cliente ${header.numeroSuministro}: ${body}")
			 .end();                
    }
}
