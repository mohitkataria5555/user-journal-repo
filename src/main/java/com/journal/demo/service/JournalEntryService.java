package com.journal.demo.service;

import com.journal.demo.Repository.JournalRepository;
import com.journal.demo.model.JournalEntry;
import com.journal.demo.model.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    // CREATE - Save a new journal entry
    @Transactional
    public void saveEntry(JournalEntry entry, String userName) {
        User user = userService.findByUserName(userName);
        JournalEntry saved = journalEntryRepository.save(entry);
        user.getJournalEntries().add(saved);
        userService.saveEntry(user);
    }

    public void saveEntry(JournalEntry entry) {
        journalEntryRepository.save(entry);
    }

    // READ - Get all journal entries
    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    // READ - Get a single entry by ID
    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    // UPDATE - Update an existing entry
    public JournalEntry updateEntry(ObjectId id, JournalEntry updatedEntry) {
        Optional<JournalEntry> existingEntry = journalEntryRepository.findById(id);

        if (existingEntry.isPresent()) {
            JournalEntry entry = existingEntry.get();
            entry.setTitle(updatedEntry.getTitle());
            entry.setContent(updatedEntry.getContent());
            entry.setDate(updatedEntry.getDate());
            return journalEntryRepository.save(entry);
        }

        return null; // Or throw custom exception
    }

    // DELETE - Delete an entry by ID
    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }

    // DELETE - Delete all entries
    public void deleteAllEntries() {
        journalEntryRepository.deleteAll();
    }

    // COUNT - Get total number of entries
    public long countEntries() {
        return journalEntryRepository.count();
    }

    // EXISTS - Check if entry exists
    public boolean entryExists(ObjectId id) {
        return journalEntryRepository.existsById(id);
    }
}