package fr.pantheonsorbonne.miage.resouces;


import fr.pantheonsorbonne.miage.dto.Ticket;
import fr.pantheonsorbonne.miage.service.Exception.TicketAbsentException;
import fr.pantheonsorbonne.miage.service.Exception.TicketAlreadyUsedException;
import fr.pantheonsorbonne.miage.service.TicketService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/ticket/{vendorId}/{concertId}")
public class TicketRessource {

    @Inject
    TicketService ticketService;

    @GET
    @Path("/{placeId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Ticket getTicket(@PathParam("vendorId") int vendorId, @PathParam("concertId") int concertId, @PathParam("placeId") int placeId) {
        try{
            return ticketService.getTicket(vendorId,concertId,placeId);
        }
        catch(TicketAbsentException e){
            throw new WebApplicationException("ticket absent avec ce vendorID, ce concertId et cette place", 404);
        }
    }

    @PUT
    @Path("/generateTicket/{placeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateTicket(@PathParam("vendorId") int vendorId, @PathParam("concertId") int concertId, @PathParam("placeId") int placeId) throws TicketAbsentException {
        try{
            ticketService.generateTicket(vendorId, concertId, placeId);
        }
        catch (TicketAlreadyUsedException e){
            throw new WebApplicationException("ticket is already booked", 409);
        }
        return Response.accepted()
                .entity(ticketService.getTicket(vendorId,concertId,placeId))
                .build();
    }
    @GET
    @Path("/allTicketsForConcert")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Ticket> getAllTicketsForConcert(@PathParam("concertId") int concertId, @PathParam("vendorId") int vendorId){
        return ticketService.getAllTicketsForConcert(vendorId, concertId);
    }
}
