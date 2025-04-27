package malla.dipesh.journalApp.service;

import malla.dipesh.journalApp.model.JournalEntry;
import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class JournalEntryService {

    public final JournalEntryRepository journalEntryRepository;
    private final UserService userService;


    public JournalEntryService(JournalEntryRepository journalEntryRepository, UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
       try{
           User user = userService.findByUserName(userName);
           journalEntry.setDateCreated(LocalDateTime.now());
           JournalEntry saved = journalEntryRepository.save(journalEntry);
           user.getJournalEntries().add(saved);
           userService.saveUser(user);
       }catch (Exception e){
              throw new RuntimeException("Failed to save journal entry");
       }
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry findById(ObjectId id) {
        return journalEntryRepository.findById(id).orElseThrow(() -> new RuntimeException("Entry not found"));
    }

    @Transactional
    public void deleteById(ObjectId id,String username) {
        User user = userService.findByUserName(username);
        boolean removed = user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
        if(removed) {
            userService.saveUser(user);
            journalEntryRepository.deleteById(id);
        }
    }


    public JournalEntry updateById(ObjectId id,JournalEntry newEntry) {
        JournalEntry old = journalEntryRepository.findById(id).orElseThrow(() -> new RuntimeException("Entry not found"));
        if(old == null){
            return null;
        }
        old.setTitle(newEntry.getTitle()!=null&& !newEntry.getTitle().isEmpty() ?newEntry.getTitle():old.getTitle());
        old.setContent(newEntry.getContent()!=null&& !newEntry.getContent().isEmpty() ?newEntry.getContent():old.getContent());
        return journalEntryRepository.save(old);
    }

    public JournalEntry findByTitle(String title) {
        return journalEntryRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Entry not found"));
    }
}
