package fr.pantheonsorbonne.miage.service;


import fr.pantheonsorbonne.miage.dao.TicketDAO;
import fr.pantheonsorbonne.miage.dto.Ticket;
import fr.pantheonsorbonne.miage.service.Exception.TicketAbsentException;
import fr.pantheonsorbonne.miage.service.Exception.TicketAlreadyUsedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class TicketService {

    @Inject
    TicketDAO ticketDAO;

    public void generateTicket(int vendorId, int concertId, int placeId) throws TicketAlreadyUsedException{

        Optional<Ticket> t = ticketDAO.getTickets().stream()
                .filter(ticket -> ticket.concertId() == concertId)
                .filter(ticket -> ticket.placeId() == placeId)
                .filter(ticket -> ticket.vendorId() == vendorId)
                .findAny();
        if(t.isPresent()){
            throw new TicketAlreadyUsedException();
        }
        else {
            byte[] array = new byte[7]; // length is bounded by 7
            new Random().nextBytes(array);
            String generatedString = new String(array, StandardCharsets.UTF_8);
            ticketDAO.getTickets().add(new Ticket(vendorId,concertId,placeId, generatedString));
        }
    }

    public Ticket getTicket(int vendorId, int concertId, int placeId) throws TicketAbsentException{

        Optional<Ticket> t = ticketDAO.getTickets().stream()
                .filter(ticket -> ticket.vendorId() == vendorId)
                .filter(ticket -> ticket.concertId() == concertId)
                .filter(ticket -> ticket.placeId() == placeId)
                .findAny();
        return t.orElseThrow(TicketAbsentException::new);
    }

    public Set<Ticket> getAllTicketsForConcert(int vendorId, int concertId){
        return ticketDAO.getTickets().stream()
                .filter(ticket ->ticket.vendorId() == vendorId)
                .filter(ticket -> ticket.concertId() == concertId)
                .collect(Collectors.toSet());
    }

}
