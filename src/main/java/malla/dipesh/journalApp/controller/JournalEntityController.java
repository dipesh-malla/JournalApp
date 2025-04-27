package malla.dipesh.journalApp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import malla.dipesh.journalApp.model.JournalEntry;
import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.service.JournalEntryService;
import malla.dipesh.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
@Tag(name = "Journal APIs", description = "all about jounral endpoints") // for swagger
public class JournalEntityController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    public JournalEntityController(JournalEntryService journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }


    @GetMapping("/all")
    @Operation(summary = "get all the journal entries ")
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        List<JournalEntry> all = journalEntryService.getAll();
        if(all.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/userJournal")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@RequestParam("username") String username) {

            User user = userService.findByUserName(username);
            List<JournalEntry> all = user.getJournalEntries();
            if(all.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(all, HttpStatus.OK);

    }


    @PostMapping
    public ResponseEntity<?> createEntry(
            @RequestBody JournalEntry journalEntry
            )  {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            journalEntryService.saveEntry(journalEntry, username);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(
         @PathVariable("id") ObjectId id
    ) {

      try{
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          String username = authentication.getName();
          User user = userService.findByUserName(username);
          List<JournalEntry> allUserJournal = user.getJournalEntries().stream().filter(journalEntry -> journalEntry.getId().equals(id)).toList();
          if(allUserJournal.isEmpty()) {
              return new ResponseEntity<>(HttpStatus.NO_CONTENT);
          }
            JournalEntry journalEntry = journalEntryService.findById(id);
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
      }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
      }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable("id")   ObjectId id
    ) {
         try{
             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
             journalEntryService.deleteById(id,username);
             return new ResponseEntity<>( HttpStatus.NO_CONTENT);
         }catch (RuntimeException e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
         }
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id")   ObjectId id,
            @RequestBody JournalEntry newEntry
    ) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUserName(username);
            List<JournalEntry> allUserJournal = user.getJournalEntries().stream().filter(journalEntry -> journalEntry.getId().equals(id)).toList();
            if(allUserJournal.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(journalEntryService.updateById(id, newEntry), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
