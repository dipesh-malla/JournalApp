package malla.dipesh.journalApp.cache;

import jakarta.annotation.PostConstruct;
import malla.dipesh.journalApp.model.ConfigJournalApp;
import malla.dipesh.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys {
        WEATHER_API
    }

    public Map<String, String> APP_CACHE = new HashMap<>();
    private final ConfigJournalAppRepository repository;

    public AppCache(ConfigJournalAppRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    @Scheduled(cron = "0 */15 * * * *")
    public void init() {
        List<ConfigJournalApp> all = repository.findAll();
        for (ConfigJournalApp app : all) {
            APP_CACHE.put(app.getKey(), app.getValue());
        }
    }
}
