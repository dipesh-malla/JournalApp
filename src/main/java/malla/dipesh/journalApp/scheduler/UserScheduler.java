package malla.dipesh.journalApp.scheduler;


import malla.dipesh.journalApp.model.JournalEntry;
import malla.dipesh.journalApp.model.User;
import malla.dipesh.journalApp.service.EmailService;
import malla.dipesh.journalApp.service.SentimentAnalysisService;
import malla.dipesh.journalApp.service.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;

    @Scheduled(cron = "0 0 9 * * SUN")
//    @Scheduled(cron = "0 * * * * *")
    public void fetchUsersAndSendSaMail(){
        List<User> users = userRepositoryImpl.getUserForSA();
        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<JournalEntry> filteredEntries  = journalEntries.stream().filter(x -> x.getDateCreated().isAfter(LocalDateTime.now().minusDays(7))).toList();
            String content = filteredEntries.stream().map(JournalEntry::getContent).collect(Collectors.joining("\n "));
            String entry = String.join( "",  content);
            String sentiment = sentimentAnalysisService.getSentiment(entry);
            emailService.sendMail(user.getEmail(), "Sentiment for last 7 days", sentiment);
        }
    }

}
