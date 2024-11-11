package fr.pantheonsorbonne.miage.resouces;

import fr.pantheonsorbonne.miage.dto.Booking;
import fr.pantheonsorbonne.miage.dto.CreateQuota;
import fr.pantheonsorbonne.miage.dto.Quota;
import fr.pantheonsorbonne.miage.service.Exception.*;
import fr.pantheonsorbonne.miage.service.QuotaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("concerts/{vendorId}/quotas")
public class QuotaResource {

    @Inject
    QuotaService quotaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getQuotas(){
        return "bienvenue dans la partie quotas de l'API";
    }

    @GET
    @Path("/{concertId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Quota getQuotas(@PathParam("vendorId") int vendorId, @PathParam("concertId") int concertId) {
        try {
            return quotaService.getQuota(vendorId, concertId);
        }catch(NoQuotaForVendorAndConcertException e){
            throw new WebApplicationException("no sutch quota for vendor or concert", 404);
        }
    }

    @GET
    @Path("/getAllQuotasForVendor")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Quota> getAllQuotasForVendor(@PathParam("vendorId") int vendorId){
        return quotaService.getAllQuotasForVendor(vendorId);
    }

    @PUT
    @Path("/consumeQuotaForConcert/{concertId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeQuotasForConcert(@PathParam("vendorId") int vendorId, @PathParam("concertId") int concertId, Booking booking) throws NoQuotaForVendorAndConcertException {
        try {
            quotaService.consumeQuota(vendorId, concertId, booking.seating(), booking.standing());
        }
        catch (NoQuotaForVendorAndConcertException e){
            throw new WebApplicationException("no sutch quota for vendor or concert", 404);
        }
        catch(InsufficientQuotaException e){
            throw new WebApplicationException("insuffisant quota", 400);
        }
        return Response.accepted()
                .entity(quotaService.getQuota(vendorId, concertId))
                .build();
    }
    @POST
    @Path("/create new/{concertId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewQuota(@PathParam("vendorId") int vendorId, @PathParam("concertId") int concertId, CreateQuota createQuota) throws NoQuotaForVendorAndConcertException {
        try{
            quotaService.createQuota(vendorId, concertId, createQuota.seated(), createQuota.standing());
        }
        catch(AlreadyExistingQuota e){
            throw new WebApplicationException(" quota already exists", 402);
        }
        return Response.accepted()
                .entity(quotaService.getQuota(vendorId, concertId))
                .build();
    }
}
