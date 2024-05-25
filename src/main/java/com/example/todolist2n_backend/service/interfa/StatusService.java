package com.example.todolist2n_backend.service.interfa;

import com.example.todolist2n_backend.model.common.ResponseApi;
import com.example.todolist2n_backend.model.request.StatusRequest;
import org.springframework.stereotype.Service;

@Service

public interface StatusService {
    ResponseApi getStatus(String textSearch, Long currentPage, Long limit, String sortData, String sortType);
    ResponseApi addStatus(StatusRequest statusRequest, Long limit);
    ResponseApi updateStatus(Long idStatus, StatusRequest statusRequest);
    ResponseApi deleteStatus(Long idStatus, Long limit, Long currentPage);
    ResponseApi getAllStatus();
}
