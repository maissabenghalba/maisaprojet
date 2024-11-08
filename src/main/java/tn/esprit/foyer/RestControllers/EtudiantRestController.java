package tn.esprit.foyer.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.foyer.DAO.Entities.Etudiant;
import tn.esprit.foyer.Services.Etudiant.IEtudiantService;

import java.util.List;

@RestController
@RequestMapping("etudiant")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")  // Allow CORS from localhost:4200 for all methods
public class EtudiantRestController {
    IEtudiantService service;

    @PostMapping("addOrUpdate")
    public Etudiant addOrUpdate(@RequestBody Etudiant e) {
        return service.addOrUpdate(e);
    }

    @GetMapping("findAll")
    public List<Etudiant> findAll() {
        return service.findAll();
    }

    @GetMapping("findById")
    public Etudiant findById(@RequestParam long id) {
        return service.findById(id);
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Etudiant e) {
        service.delete(e);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) {
        service.deleteById(id);
    }
}
