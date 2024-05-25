package com.example.todolist2n_backend.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class StatusRequest {
    private String nameStatus;

    private String description;
}

