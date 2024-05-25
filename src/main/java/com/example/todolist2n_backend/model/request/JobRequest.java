package com.example.todolist2n_backend.model.request;

import com.example.todolist2n_backend.model.constant.PriorityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class JobRequest {
    private String nameJob;

    private Long idStatus;

    private String nameStatus;

    private PriorityEnum priority;
}
