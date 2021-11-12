package edesur.hurto.inspecciones;

import edesur.hurto.inspecciones.model.SuministroResultado;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BeanAggregationStrategy implements AggregationStrategy {
    private static final Logger logger = LoggerFactory.getLogger(BeanAggregationStrategy.class);

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (newExchange == null) {
            return oldExchange;
        }

        SuministroResultado resultado = oldExchange.getIn().getBody(SuministroResultado.class);
        Map data = newExchange.getIn().getBody(Map.class);

        if (data != null) {
            String property = (String) data.get("property");
            Object valor = data.get("valor");
            logger.debug("property: {}, valor {}", property, valor);
            if (valor != null) {
                try {
                    BeanUtils.setProperty(resultado, (String) data.get("property"), data.get("valor"));
                }
                catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
            }
        }
        else {
            logger.debug("No hay datos");
        }

        return oldExchange;
    }
}
