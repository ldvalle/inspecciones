package edesur.hurto.inspecciones.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanPropertyAggregationStrategy<T> implements AggregationStrategy {
    private static final Logger logger = LoggerFactory.getLogger(BeanPropertyAggregationStrategy.class);

    private final String propertyName;

    public BeanPropertyAggregationStrategy(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (newExchange == null) {
            return oldExchange;
        }

        @SuppressWarnings("unchecked") T resultado = (T) oldExchange.getIn().getBody();
        Object data = newExchange.getIn().getBody();

        String className = resultado.getClass().getSimpleName();

        if (data == null) {
            logger.debug("No hay datos para {}.{}", className, propertyName);
        }
        else {
            logger.debug("{}.{} = {}", className, propertyName, data);
            try {
                BeanUtils.setProperty(resultado, propertyName, data);
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        return oldExchange;
    }
}
