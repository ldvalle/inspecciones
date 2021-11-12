package edesur.hurto.inspecciones.routes;

import edesur.hurto.inspecciones.model.SuministroResponse;
import edesur.hurto.inspecciones.model.SuministroResultado;
import org.apache.camel.Body;

public class SuministroResponseBuilder {
    public SuministroResponse build(@Body SuministroResultado resultado) {
        SuministroResponse response = new SuministroResponse();
        response.getListaResultado().add(resultado);
        return response;
    }
}
