package tn.esprit.foyer.Services.Chambre;


import tn.esprit.foyer.DAO.Entities.Chambre;
import tn.esprit.foyer.DAO.Entities.TypeChambre;

import java.util.List;

public interface IChambreService {
    Chambre addOrUpdate(Chambre c);
    List<Chambre> findAll();
    Chambre findById(long id);
    void deleteById(long id);
    void delete(Chambre c);


   List<Chambre>  getChambresParNomBloc( String nomBloc);



    long  nbChambreParTypeEtBloc(TypeChambre type, long idBloc);
    List<Chambre>  getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type);
    void listeChambresParBloc();
    void pourcentageChambreParTypeChambre();
    void nbPlacesDisponibleParChambreAnneeEnCours();

}
