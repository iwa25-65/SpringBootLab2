package pl.dmcs.rkotas.springbootlab2.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.dmcs.rkotas.springbootlab2.model.Student;
import pl.dmcs.rkotas.springbootlab2.model.Team;
import pl.dmcs.rkotas.springbootlab2.repository.StudentRepository;
import pl.dmcs.rkotas.springbootlab2.repository.TeamRepository;

import java.util.List;


@RestController
@RequestMapping("/teams")
public class TeamRESTController {

    private StudentRepository studentRepository;
    private TeamRepository teamRepository;

    @Autowired
    public TeamRESTController(StudentRepository studentRepository, TeamRepository teamRepository) {
        this.studentRepository = studentRepository;
        this.teamRepository = teamRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        teamRepository.save(team);
        return new ResponseEntity<Team>(team, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Team> deleteTeam(@PathVariable("id") long id) {
        Team team = teamRepository.findById(id);
        if (team == null) {
            System.out.println("Team not found!");
            return new ResponseEntity<Team>(HttpStatus.NOT_FOUND);
        }
        teamRepository.deleteById(id);
        return new ResponseEntity<Team>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Team> updateTeam(@RequestBody Team team, @PathVariable("id") long id) {
        team.setId(id);
        teamRepository.save(team);
        return new ResponseEntity<Team>(team, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Team> findTeam(@PathVariable("id") long id) {
        Team team = teamRepository.findById(id);
        if (team == null) {
            System.out.println("Team not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<List<Team>> updateAllTeams(@RequestBody List<Team> teams) {
        List<Team> savedTeams = teamRepository.saveAll(teams);
        return new ResponseEntity<>(savedTeams, HttpStatus.OK);
    }


}