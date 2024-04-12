package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void insert(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileId} AND userid = #{userId}")
    void delete(Integer fileId, Integer userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getAllFilesByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId} AND userid = #{userId}")
    File getFileByFileId(Integer fileId, Integer userId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    File getFileByFileName(String fileName, Integer userId);
}
