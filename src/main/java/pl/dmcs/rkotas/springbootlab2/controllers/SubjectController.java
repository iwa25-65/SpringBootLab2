package pl.dmcs.rkotas.springbootlab2.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.dto.SubjectDTO;
import pl.dmcs.rkotas.springbootlab2.model.Subject;
import pl.dmcs.rkotas.springbootlab2.service.SubjectService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        SubjectDTO dto = subjectService.getSubjectById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@RequestBody SubjectDTO subjectDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        SubjectDTO created = subjectService.createSubjectForTeacher(subjectDTO, username);
        return ResponseEntity.created(URI.create("/api/subjects/" + created.getId())).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> updateSubject(@PathVariable Long id, @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO updated = subjectService.updateSubject(id, subjectDTO);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        if (subjectService.deleteSubject(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
