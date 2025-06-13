package com.sam.journalApp.Controller;

import com.sam.journalApp.Entity.JournalEntry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private Map<Long, JournalEntry> journalEntries = new HashMap<>();
    //
     //
    @GetMapping("/getAll")
    public List<JournalEntry> getAll(){
        return new ArrayList<>(journalEntries.values());
    }


    @GetMapping("/getPostById")
    public List<JournalEntry> getPostById(){
        return new ArrayList<>(journalEntries.values());
    }
    @GetMapping("/getPostBy")
    public List<JournalEntry> getPostBy(){
        return new ArrayList<>(journalEntries.values());
    }

}

