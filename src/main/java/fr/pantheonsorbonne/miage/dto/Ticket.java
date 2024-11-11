package fr.pantheonsorbonne.miage.dto;

public record Ticket(int vendorId, int concertId, int placeId, String ticketToken) {
}
