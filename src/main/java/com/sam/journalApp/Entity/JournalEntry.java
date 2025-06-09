package com.sam.journalApp.Entity;

import lombok.Data;
// POJO - > Class - Plane Old Java Object
@Data
public class JournalEntry {
    private long id;
    private String title;
    private String content;
}
