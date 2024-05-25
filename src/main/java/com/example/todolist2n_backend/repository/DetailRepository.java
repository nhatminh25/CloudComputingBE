package com.example.todolist2n_backend.repository;

import com.example.todolist2n_backend.model.entity.DetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface DetailRepository extends JpaRepository<DetailEntity, Long> {
    @Query("select detail from DetailEntity detail where detail.job.idJob = :idJob")
    List<DetailEntity> getListDetail(Long idJob);

    @Query("select d from DetailEntity d where d.job.idJob=:idJob and d.nameDetail like %:key%")
    Page<DetailEntity> searchDetail(@Param("key") String textSearch, @Param("idJob") Long idJob, Pageable pageable);

    @Query("select detail from DetailEntity detail where detail.status.idStatus = :idStatus")
    List<DetailEntity> getListDetailbyStatus(Long idStatus);

    @Query("select detail from DetailEntity detail where detail.job.idJob = :idJob")
    List<DetailEntity> findByIdJob(Long idJob);

    @Query ("select detail from DetailEntity detail where detail.job.idJob = :idJob and detail.status.idStatus = :l")
    List<DetailEntity> findByIdJobAndStatus(Long idJob, Long l);
}
