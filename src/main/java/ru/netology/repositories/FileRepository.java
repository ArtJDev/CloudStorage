package ru.netology.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.netology.entities.File;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    @Query(value = "select f from files f where f.username = :username order by f.filename limit :limit", nativeQuery = true)
    List<File> findAllByUsername (@Param("username") String username, int limit);
    File findByFilenameAndUsername(String filename, String username);
}