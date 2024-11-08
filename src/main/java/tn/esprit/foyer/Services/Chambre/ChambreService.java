package tn.esprit.foyer.Services.Chambre;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.foyer.DAO.Entities.Bloc;
import tn.esprit.foyer.DAO.Entities.Chambre;
import tn.esprit.foyer.DAO.Entities.Reservation;
import tn.esprit.foyer.DAO.Entities.TypeChambre;
import tn.esprit.foyer.DAO.Repositories.BlocRepository;
import tn.esprit.foyer.DAO.Repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService   {
    private final ChambreRepository repo;
    private final BlocRepository blocRepository;

    private static final String MSG_PLACE_DISPONIBLE = "Le nombre de place disponible pour la chambre";
    private static final String MSG_CHAMBRE_COMPLETE = "est complete";
    private static final String MSG_LA_CHAMBRE = "La chambre";

    @Override
    public Chambre addOrUpdate(Chambre c) {
        return repo.save(c);
    }

    @Override
    public List<Chambre> findAll() {
        return repo.findAll();
    }

    @Override
    public Chambre findById(long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Chambre c) {
        repo.delete(c);
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        return repo.findByBlocNomBloc(nomBloc);
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        return repo.countByTypeCAndBlocIdBloc(type, idBloc);
    }

    private LocalDate[] getAnneeUniversitaire() {
        int year = LocalDate.now().getYear() % 100;
        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
        return new LocalDate[]{dateDebutAU, dateFinAU};
    }

    @Override
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {

        LocalDate[] anneeUniversitaire = getAnneeUniversitaire();
        LocalDate dateDebutAU = anneeUniversitaire[0];
        LocalDate dateFinAU = anneeUniversitaire[1];

        List<Chambre> listChambreDispo = new ArrayList<>();
        for (Chambre c : repo.findAll()) {
            if (isChambreEligibleForReservation(c, nomFoyer, type, dateDebutAU, dateFinAU)) {
                listChambreDispo.add(c);
            }
        }
        return listChambreDispo;
    }

    private boolean isChambreEligibleForReservation(Chambre c, String nomFoyer, TypeChambre type, LocalDate dateDebutAU, LocalDate dateFinAU) {
        if (!c.getTypeC().equals(type) || !c.getBloc().getFoyer().getNomFoyer().equals(nomFoyer)) {
            return false;
        }

        int numReservation = countReservationsInAnneeUniversitaire(c, dateDebutAU, dateFinAU);
        switch (type) {
            case SIMPLE:
                return numReservation == 0;
            case DOUBLE:
                return numReservation < 2;
            case TRIPLE:
                return numReservation < 3;
            default:
                return false;
        }
    }

    private int countReservationsInAnneeUniversitaire(Chambre c, LocalDate dateDebutAU, LocalDate dateFinAU) {
        int numReservation = 0;
        for (Reservation reservation : c.getReservations()) {
            if (reservation.getAnneeUniversitaire().isBefore(dateFinAU) && reservation.getAnneeUniversitaire().isAfter(dateDebutAU)) {
                numReservation++;
            }
        }
        return numReservation;
    }

    @Override
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => " + b.getNomBloc() + " ayant une capacité " + b.getCapaciteBloc());
            if (!b.getChambres().isEmpty()) {
                log.info("La liste des chambres pour ce bloc: ");
                for (Chambre c : b.getChambres()) {
                    log.info("NumChambre: " + c.getNumeroChambre() + " type: " + c.getTypeC());
                }
            } else {
                log.info("Pas de chambre disponible dans ce bloc");
            }
            log.info("********************");
        }
    }

    @Override
    public void pourcentageChambreParTypeChambre() {
        long totalChambre = repo.count();
        double pSimple = (double) (repo.countChambreByTypeC(TypeChambre.SIMPLE) * 100) / totalChambre;
        double pDouble = (double) (repo.countChambreByTypeC(TypeChambre.DOUBLE) * 100) / totalChambre;
        double pTriple = (double) (repo.countChambreByTypeC(TypeChambre.TRIPLE) * 100) / totalChambre;
        log.info("Nombre total des chambre: " + totalChambre);
        log.info("Le pourcentage des chambres pour le type SIMPLE est égale à " + pSimple);
        log.info("Le pourcentage des chambres pour le type DOUBLE est égale à " + pDouble);
        log.info("Le pourcentage des chambres pour le type TRIPLE est égale à " + pTriple);

    }

    @Override
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
        LocalDate[] anneeUniversitaire = getAnneeUniversitaire();
        LocalDate dateDebutAU = anneeUniversitaire[0];
        LocalDate dateFinAU = anneeUniversitaire[1];

        for (Chambre c : repo.findAll()) {
            logNbPlacesDisponible(c, dateDebutAU, dateFinAU);
        }
    }

    private void logNbPlacesDisponible(Chambre c, LocalDate dateDebutAU, LocalDate dateFinAU) {
        long nbReservation = repo.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                c.getIdChambre(), true, dateDebutAU, dateFinAU
        );

        switch (c.getTypeC()) {
            case SIMPLE:
                log.info(nbReservation == 0 ? MSG_PLACE_DISPONIBLE + " " + c.getTypeC() + " " + c.getNumeroChambre() + " est 1" : MSG_LA_CHAMBRE + " " + c.getTypeC() + " " + c.getNumeroChambre() + " " + MSG_CHAMBRE_COMPLETE);
                break;
            case DOUBLE:
                log.info(nbReservation < 2 ? MSG_PLACE_DISPONIBLE + " " + c.getTypeC() + " " + c.getNumeroChambre() + " est " + (2 - nbReservation) : MSG_LA_CHAMBRE + " " + c.getTypeC() + " " + c.getNumeroChambre() + " " + MSG_CHAMBRE_COMPLETE);
                break;
            case TRIPLE:
                log.info(nbReservation < 3 ? MSG_PLACE_DISPONIBLE + " " + c.getTypeC() + " " + c.getNumeroChambre() + " est " + (3 - nbReservation) : MSG_LA_CHAMBRE + " " + c.getTypeC() + " " + c.getNumeroChambre() + " " + MSG_CHAMBRE_COMPLETE);
                break;
        }
    }

}
