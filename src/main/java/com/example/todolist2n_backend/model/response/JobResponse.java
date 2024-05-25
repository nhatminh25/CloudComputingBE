package com.example.todolist2n_backend.model.response;

import com.example.todolist2n_backend.model.constant.PriorityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobResponse {
    private Long idJob;

    private String nameJob;

    private String progress;

    private double progressDouble;

    private Date createdAt;

    private Date updatedAt;

    private boolean hasDetail;

    private PriorityEnum priority;

    private String nameStatus;

    private Long idStatus;

}