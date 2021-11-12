package edesur.hurto.inspecciones.validacion;

import edesur.hurto.inspecciones.Configuracion;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = EmpresaValidator.class)
@Documented
public @interface CheckEmpresa {
    String message() default "El código de empresa debe ser " + Configuracion.id_empresa;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    boolean obligatorio() default true;
}
