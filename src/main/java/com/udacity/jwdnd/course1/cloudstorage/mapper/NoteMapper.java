package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    void insert(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId} AND userid = #{userId}")
    void update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND userid = #{userId}")
    void delete(Integer noteId, Integer userId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    Note getNoteById(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getAllNotesByUserId(Integer userId);
}
