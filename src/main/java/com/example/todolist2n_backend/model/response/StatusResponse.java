package com.example.todolist2n_backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {
    private Long idStatus;

    private String nameStatus;

    private String description;

    private boolean isDefault;
}