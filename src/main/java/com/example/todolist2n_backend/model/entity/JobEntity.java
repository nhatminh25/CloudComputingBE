package com.example.todolist2n_backend.model.entity;

import com.example.todolist2n_backend.model.constant.PriorityEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job")
@Data
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJob;

    private String nameJob;

    private String progress;

    private double progressDouble;

    private Date createdAt;

    private Date updatedAt;

    private boolean hasDetail;

    private PriorityEnum priority;

    @OneToMany (mappedBy = "job",cascade = CascadeType.ALL)
    private List<DetailEntity> detailJobEntities;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private StatusEntity status;
}