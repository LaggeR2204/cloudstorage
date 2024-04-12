package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void createNewNote(Note note) {
        noteMapper.insert(new Note(null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }

    public void updateNote(Note note) {
        noteMapper.update(note);
    }

    public List<Note> getNotesByUserId(Integer userId) {
        return noteMapper.getAllNotesByUserId(userId);
    }

    public void deleteNoteByNoteId(Integer noteId, Integer userId) {
        noteMapper.delete(noteId, userId);
    }
}
