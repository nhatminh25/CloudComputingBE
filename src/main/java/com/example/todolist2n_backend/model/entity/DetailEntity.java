package com.example.todolist2n_backend.model.entity;

import com.example.todolist2n_backend.model.constant.PriorityEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="detail")
public class DetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idDetail;

    private String nameDetail;

    @ManyToOne
    @JoinColumn(name = "id_job")
    private JobEntity job;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private StatusEntity status;;

    private PriorityEnum priority;

    private Date createdAt;

    private Date updatedAt;
}
