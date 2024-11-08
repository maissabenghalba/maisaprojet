package tn.esprit.foyer.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.foyer.DAO.Entities.Reservation;
import tn.esprit.foyer.Services.Reservation.IReservationService;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("reservation")
@AllArgsConstructor
public class ReservationRestController {
    IReservationService service;

    @PostMapping("addOrUpdate")
    public Reservation addOrUpdate(@RequestBody Reservation r) {
        return service.addOrUpdate(r);
    }

    @GetMapping("findAll")
    public List<Reservation> findAll() {
        return service.findAll();
    }

    @GetMapping("findById")
    public Reservation findById(@RequestParam String id) {
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Reservation r) {
        service.delete(r);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam String id) {
        service.deleteById(id);
    }

        @PostMapping("ajouterReservationEtAssignerAChambreEtAEtudiant")
        public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(@RequestParam Long numChambre, @RequestParam long cin) {
            return service.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
        }


    @GetMapping("getReservationParAnneeUniversitaire")
    public long getReservationParAnneeUniversitaire(@RequestParam LocalDate debutAnnee, @RequestParam LocalDate finAnnee) {
        return service.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);
    }


    @DeleteMapping("annulerReservation")
    public String annulerReservation(@RequestParam long cinEtudiant) {
        return service.annulerReservation(cinEtudiant);
    }


}