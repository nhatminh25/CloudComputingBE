package com.example.todolist2n_backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "status")
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatus;

    private String nameStatus;

    private String description;

    @OneToMany (mappedBy = "status")
    private List<DetailEntity> detailJobEntities;

    @OneToMany (mappedBy = "status")
    private List<JobEntity> jobEntities;

    private boolean isDefault;
}