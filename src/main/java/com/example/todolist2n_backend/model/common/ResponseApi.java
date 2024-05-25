package com.example.todolist2n_backend.model.common;


import com.example.todolist2n_backend.model.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResponseApi {
    private String message ;
    private Content content;
    private Boolean status;
}
