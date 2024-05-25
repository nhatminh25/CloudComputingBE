package com.example.todolist2n_backend.mapping;

import com.example.todolist2n_backend.model.entity.DetailEntity;
import com.example.todolist2n_backend.model.request.DetailRequest;
import com.example.todolist2n_backend.model.response.DetailResponse;
import lombok.Data;

@Data

public class DetailMapping {
    public static DetailEntity mapRequestToEntity(DetailRequest detailRequest){
        DetailEntity detailEntity = new DetailEntity();
        detailEntity.setNameDetail(detailRequest.getNameDetail());
        detailEntity.setPriority(detailRequest.getPriority());
        return detailEntity;
    }
    public static DetailResponse mapEntityToResponse(DetailEntity detailEntity){
        DetailResponse detailResponse= new DetailResponse();
        detailResponse.setIdDetail(detailEntity.getIdDetail());
        detailResponse.setNameDetail(detailEntity.getNameDetail());
        detailResponse.setIdJob(detailEntity.getJob().getIdJob());
        detailResponse.setCreatedAt(detailEntity.getCreatedAt());
        detailResponse.setUpdatedAt(detailEntity.getUpdatedAt());
        detailResponse.setPriority(detailEntity.getPriority());
        detailResponse.setIdStatus(detailEntity.getStatus().getIdStatus());
        detailResponse.setNameStatus(detailEntity.getStatus().getNameStatus());
        return detailResponse;
    }
}