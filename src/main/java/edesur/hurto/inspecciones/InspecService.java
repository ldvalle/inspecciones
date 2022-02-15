package edesur.hurto.inspecciones;

import edesur.hurto.inspecciones.model.InspeSolicitudRequest;
import edesur.hurto.inspecciones.model.InspeSolicitudResponse;
import edesur.hurto.inspecciones.model.InspeStatusRequest;
import edesur.hurto.inspecciones.model.InspeStatusResponse;
import edesur.hurto.inspecciones.model.InspeStatusRequest2;
import edesur.hurto.inspecciones.model.InspeHistoRequest;
import edesur.hurto.inspecciones.model.InspeHistoResultado;
import edesur.hurto.inspecciones.model.InspeHistoResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class InspecService {

    @POST
    @Path("/SolicitudInspeccion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspeSolicitudResponse SolicitudInspeccion(@NotNull @Valid InspeSolicitudRequest request) {
        return null;
    }

    @POST
    @Path("/ConsultaInspeccion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspeStatusResponse ConsultaInspeccion(@NotNull @Valid InspeStatusRequest request) {
        return null;
    }

    @POST
    @Path("/ConsultaInspeccion2")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspeStatusResponse ConsultaInspeccion2(@NotNull @Valid InspeStatusRequest2 request) {
        return null;
    }

    @POST
    @Path("/getWorkOrderID")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspeHistoResponse getWorkOrderID(@NotNull @Valid InspeHistoRequest request) {
        return null;
    }

}
