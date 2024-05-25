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
public class DetailResponse {
    private Long idDetail;

    private String nameDetail;

    private Long idJob;

    private String nameStatus;

    private Long idStatus;

    private PriorityEnum priority;

    private Date createdAt;

    private Date updatedAt;
}