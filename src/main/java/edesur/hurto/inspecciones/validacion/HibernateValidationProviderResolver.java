package edesur.hurto.inspecciones.validacion;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ValidationProviderResolver;
import java.util.Collections;
import java.util.List;

public class HibernateValidationProviderResolver implements ValidationProviderResolver {
    @SuppressWarnings("unchecked")
    @Override
    public List getValidationProviders() {
        return Collections.singletonList(new HibernateValidator());
    }
}
