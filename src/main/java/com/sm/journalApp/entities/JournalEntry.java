package com.sm.journalApp.entities;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


/*
✅ POJO stands for: Plain Old Java Object
1. ➡️ Simple Java class
2. ➡️ Has private fields
3. ➡️ Provides public getters/setters
4. ➡️ No special restrictions (don’t extend framework classes, no heavy annotations)
5. ➡️ Mainly used to hold data
*/
//@Setter
//@Getter
@Data // @Setter + @Setter + toString more
// @Document // MongoDB document for journal entries with id, title, content fields
@Document(collection = "journal_Entries")
//@NoArgsConstructor
public class JournalEntry {
    @Id // Map => Primary Key
       // marks this field as the primary key (unique identifier) in MongoDB
//    private String id;
    private ObjectId id;
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
}
