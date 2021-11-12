package edesur.hurto.inspecciones.beans;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class BeanPropertiesAggregationStrategy<T> implements AggregationStrategy {
    private static final Logger logger = LoggerFactory.getLogger(BeanPropertiesAggregationStrategy.class);

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (newExchange == null) {
            return oldExchange;
        }

        //noinspection unchecked
        T resultado = (T) oldExchange.getIn().getBody();
        Map data = newExchange.getIn().getBody(Map.class);

        String className = resultado.getClass().getSimpleName();

        if (data == null) {
            logger.debug("No hay datos para {}", className);
        }
        else {
            //noinspection unchecked
            for (String k: (Set<String>)data.keySet()) {
                Object v = data.get(k);
                String propertyName = toCamelCase(k);
                logger.debug("{}.{} = {}", className, propertyName, v);
                if (v != null) {
                    try {
                        BeanUtils.setProperty(resultado, propertyName, v);
                    }
                    catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return oldExchange;
    }

    private String toCamelCase(String value) {
        String[] strings = value.toLowerCase().split("_");
        for (int i = 1; i < strings.length; i++) {
            strings[i] = strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1);
        }
        return String.join("", strings);
    }
}
