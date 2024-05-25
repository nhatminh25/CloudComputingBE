package com.example.todolist2n_backend.repository;

import com.example.todolist2n_backend.model.entity.JobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface JobRepository extends JpaRepository<JobEntity, Long> {
    @Query("select j from JobEntity j where j.nameJob like %:key%")
    Page<JobEntity> searchJob(@Param("key") String textSearch, Pageable pageable);

    @Query("select job from JobEntity job where job.status.idStatus = :idStatus")
    List<JobEntity> getListJoblbyStatus(Long idStatus);
}
