package com.galvanize;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professionals")
public class ProfessionalsController {

    private final ProfessionalRepository db;

    public ProfessionalsController(ProfessionalRepository db) {
        this.db = db;
    }

    @GetMapping("")
    public List<Professional> getProfessionals() {
        return db.findAll();
    }

    @GetMapping("/{id}")
    public Professional getProfessional(@PathVariable Long id) throws Exception {
        return db.findOne(id);
    }

    @PostMapping("")
    public Professional createProfessional(@RequestBody Professional professional) {
        return db.save(professional);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Professional> updateProfessional(@PathVariable Long id,
                                                           @RequestBody Professional updatedProfessional) {

        Professional professional = db.findOne(id);
        if (professional == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        professional.setName(updatedProfessional.getName());
        professional.setCompany(updatedProfessional.getCompany());
        professional.setTitle(updatedProfessional.getTitle());
        professional.setEmail(updatedProfessional.getEmail());
        professional.setPassword(updatedProfessional.getPassword());
        professional.setPublicProfile(updatedProfessional.isPublicProfile());
        professional.setRole(updatedProfessional.getRole());

        db.save(professional);

        return new ResponseEntity<>(professional, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteProfessional(@PathVariable Long id) {
        db.delete(id);
    }

}
