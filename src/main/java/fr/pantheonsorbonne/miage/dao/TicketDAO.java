package fr.pantheonsorbonne.miage.dao;

import fr.pantheonsorbonne.miage.dto.Quota;
import fr.pantheonsorbonne.miage.dto.Ticket;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;


@ApplicationScoped
public class TicketDAO {

    Set<Ticket> tickets = new HashSet<>();

    public void onApplicationStat(@Observes StartupEvent event){
        this.getTickets().add(new Ticket(1,1,1,"tekrkgjdjg"));
    }
    Set<Quota> quota = new HashSet<>();

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }
}
