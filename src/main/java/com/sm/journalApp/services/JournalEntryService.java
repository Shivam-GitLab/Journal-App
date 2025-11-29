package com.sm.journalApp.services;
//Controller -> Service -> Repository -> Database

import com.sm.journalApp.entities.JournalEntry;
import com.sm.journalApp.entities.User;
import com.sm.journalApp.repositories.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
//            user.setUserName(null); // Double check this is what you want
            userService.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }
    } 


    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId myId){
        return  journalEntryRepository.findById(myId);
    }

    public void deleteById(ObjectId myId, String userName){
        User user = userService.findByUserName(userName);
//        User user2 = userService.findByUserId(myId);
        user.getJournalEntries().removeIf(x -> x.getId().equals(myId));
        userService.saveUser(user);
        journalEntryRepository.deleteById(myId);
    }

}


