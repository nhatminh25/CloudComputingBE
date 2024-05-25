package com.example.todolist2n_backend.service.interfa;

import com.example.todolist2n_backend.model.common.ResponseApi;
import com.example.todolist2n_backend.model.request.JobRequest;
import org.springframework.stereotype.Service;

@Service

public interface JobService {
    ResponseApi getJob(String textSearch, Long currentPage, Long limit, String sortData, String sortType);
    ResponseApi addJob (JobRequest jobRequest);
    ResponseApi updateJob(Long id, JobRequest jobRequest);
    ResponseApi deleteJob(Long id, Long limit, Long currentPage );
}
