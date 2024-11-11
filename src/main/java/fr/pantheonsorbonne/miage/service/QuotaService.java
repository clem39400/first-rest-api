package fr.pantheonsorbonne.miage.service;

import fr.pantheonsorbonne.miage.dao.QuotaDAO;
import fr.pantheonsorbonne.miage.dto.Quota;
import fr.pantheonsorbonne.miage.service.Exception.AlreadyExistingQuota;
import fr.pantheonsorbonne.miage.service.Exception.InsufficientQuotaException;
import fr.pantheonsorbonne.miage.service.Exception.NoQuotaForVendorAndConcertException;
import fr.pantheonsorbonne.miage.service.Exception.NoQuotaForVendorException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class QuotaService {

    @Inject
    QuotaDAO quotaDAO;

    public void consumeQuota(int vendorId, int concertId, int seated, int standing) throws NoQuotaForVendorAndConcertException, InsufficientQuotaException {

        Optional<Quota> q = quotaDAO.getQuota().stream()
                .filter(quota -> quota.vendorId() == vendorId)
                .filter(quota -> quota.concertId() == concertId)
                .findAny();
        Quota realQuota = q.orElseThrow(NoQuotaForVendorAndConcertException::new);

        int newStanding = realQuota.standing() - standing;
        int newSeated = realQuota.seated() - seated;

        if (newStanding >=0 && newSeated >= 0){
            quotaDAO.getQuota().remove(realQuota);
            quotaDAO.getQuota().add(new Quota(vendorId, newSeated, newStanding, concertId));
        }else{
            throw new InsufficientQuotaException();
        }
    }

    public Quota getQuota (int vendorId, int concertId) throws NoQuotaForVendorAndConcertException{
        Optional<Quota> q = quotaDAO.getQuota().stream()
                .filter(quota -> quota.vendorId() == vendorId)
                .filter(quota -> quota.concertId() == concertId)
                .findAny();
        return q.orElseThrow(NoQuotaForVendorAndConcertException::new);
    }

    public Set<Quota> getAllQuotasForVendor(int vendorId) {
        return quotaDAO.getQuota().stream()
                .filter(quota -> quota.vendorId() == vendorId)
                .collect(Collectors.toSet());
    }
    public void createQuota(int vendorId, int concertId, int seated, int standing) throws AlreadyExistingQuota{
        Optional<Quota> q = quotaDAO.getQuota().stream()
                .filter(quota -> quota.vendorId() == vendorId)
                .filter(quota -> quota.concertId() == concertId)
                .findAny();
        if(q.isPresent()){
            throw new AlreadyExistingQuota();
        }
        else {
            quotaDAO.getQuota().add(new Quota(vendorId, seated, standing, concertId));
        }
    }
}
