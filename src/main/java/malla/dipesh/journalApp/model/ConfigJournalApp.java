package malla.dipesh.journalApp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "config_journal_app")
@Data
@NoArgsConstructor
public class ConfigJournalApp {

    @Id
    private ObjectId id;
    @Field("key")
    private String key;
    @Field("value")
    private String value;
}
