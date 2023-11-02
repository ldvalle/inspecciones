package edesur.hurto.inspecciones;

import edesur.hurto.inspecciones.model.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
public class InspecService {

    @POST
    @Path("/SolicitudInspeccion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspeSolicitudResponse SolicitudInspeccion(@NotNull @Valid InspeSolicitudRequest request) {
        return null;
    }

    //Este anda
    @POST
    @Path("/MultiSolInspeccion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspeSolicitudResponse SolicitudMultiInspeccion(@NotNull @Valid InspeMultiSolRequest request) {
        return null;
    }

    // Este es con el Json que ellos mandaron
    @POST
    @Path("/SolicitudMasiva")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public InspeSolicitudResponse SolicitudMasiva(@NotNull @Valid List<MultiSolRequest> request) {
    //public InspeSolicitudResponse SolicitudMasiva(@NotNull @Valid ListaMultiSolRequest request) {
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

    @POST
    @Path("/getEventWO")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ConsultaWOResponse getEventWO(@NotNull @Valid ConsultaWORequest request) {
        return null;
    }

}
