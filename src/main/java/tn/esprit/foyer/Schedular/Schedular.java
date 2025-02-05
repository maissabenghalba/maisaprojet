package tn.esprit.foyer.Schedular;


import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.foyer.Services.Chambre.IChambreService;
import tn.esprit.foyer.Services.Reservation.IReservationService;

@Component
@AllArgsConstructor
public class Schedular {

   IChambreService iChambreService;
   IReservationService iReservationService;
   @Scheduled(cron = "0 * * * * *")
   void service1() {
        iChambreService.listeChambresParBloc();
    }

    @Scheduled(fixedRate = 30000)
//// 5 minutes = 300 secondes = 300000 millisecondes
    void service2() {
        iChambreService.pourcentageChambreParTypeChambre();
    }

    @Scheduled(fixedRate = 30000)
    void service3() {
        iChambreService.nbPlacesDisponibleParChambreAnneeEnCours();
    }
    @Scheduled(cron = "* * * 30 06 *")
    void service4() {
        iReservationService.annulerReservations();
    }
}
