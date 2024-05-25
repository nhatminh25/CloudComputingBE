package com.example.todolist2n_backend.repository;

import com.example.todolist2n_backend.model.entity.StatusEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    @Query("select s from StatusEntity s where s.nameStatus like %:key% ")
    Page<StatusEntity> searchStatus(@Param("key") String textSearch, Pageable pageable);
}
