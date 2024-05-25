package com.example.todolist2n_backend.service.interfa;

import com.example.todolist2n_backend.model.common.ResponseApi;
import com.example.todolist2n_backend.model.request.DetailRequest;
import org.springframework.stereotype.Service;

@Service

public interface DetailService {
    ResponseApi getDetail(Long idJob, String textSearch, Long currentPage, Long limit, String sortData, String sortType);
    ResponseApi addDetail(DetailRequest detailRequest);
    ResponseApi updateDetail(Long idDetail,DetailRequest detailRequest);
    ResponseApi deleteDetail(Long idDetail, Long idJob, Long currentPage, Long limit);
}
