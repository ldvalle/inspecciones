package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.errores.ErrorResponse;
import edesur.hurto.inspecciones.errores.ErrorType;
import org.apache.camel.LoggingLevel;

public class ActuDataComerRouteBuilder extends  BaseRouteBuilder {
    @Override
    public void configure() throws Exception {
        super.configure();

        from("direct:setDataComer")
			 .routeId("setDataComer")
			 .setHeader("numeroCliente", simple("${body.getNumeroSuministro}"))
			 .setHeader("numeroOrden", simple("${body.getNumeroOrden}"))
			 .setHeader("nombreCliente", simple("${body.getNombreCliente}"))
			 .setHeader("tipoDocumento", simple("${body.getTipoDocumento}"))
			 .setHeader("nroDocumento", simple("${body.getNroDocumento}"))
			 .setHeader("telefonoCliente", simple("${body.getTelefonoCliente}"))
			 .setHeader("eMail", simple("${body.getEmail}"))
			 .transacted()
          .log(LoggingLevel.DEBUG, logname, "Actualiza Datos Comerciales Cliente ${header.numeroCliente}")
          .setHeader("response", body())
			 .to("sql:classpath:sql/spInsMiCliente.sql?dataSource=#SynergiaDS&outputType=SelectOne&outputClass=edesur.hurto.inspecciones.model.ActuDataComerResponse")
			 .choice()
			 .when(body().isNull())
				  .log(LoggingLevel.DEBUG, logname, "Cliente ${header.numeroCliente} no existe")
				  .setBody(constant(ErrorResponse.create(ErrorType.ClienteNoExiste)))
			 .otherwise()
				  .log(LoggingLevel.DEBUG, logname, "Cliente ${header.numeroCliente}: ${body}")
			 .end();                

    }
}
