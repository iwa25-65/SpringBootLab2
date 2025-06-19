package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.dto.GradeDTO;
import pl.dmcs.rkotas.springbootlab2.service.GradeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeViewController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    public List<GradeDTO> getAllGrades() {
        return gradeService.getAllGrades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeDTO> getGradeById(@PathVariable Long id) {
        GradeDTO dto = gradeService.getGradeById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<GradeDTO> createGrade(@RequestBody GradeDTO dto) {
        GradeDTO created = gradeService.createGrade(dto);
        return ResponseEntity.created(URI.create("/api/grades/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeDTO> updateGrade(@PathVariable Long id, @RequestBody GradeDTO dto) {
        GradeDTO updated = gradeService.updateGrade(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        return gradeService.deleteGrade(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
