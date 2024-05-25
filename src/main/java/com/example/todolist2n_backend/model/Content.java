package com.example.todolist2n_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {
    private List list;

    private Integer totalRecord;

    private  Integer currentPage;

    private String warning;
}