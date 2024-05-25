package com.example.todolist2n_backend.mapping;

import com.example.todolist2n_backend.model.entity.StatusEntity;
import com.example.todolist2n_backend.model.request.StatusRequest;
import com.example.todolist2n_backend.model.response.StatusResponse;
import lombok.Data;

@Data

public class StatusMapping {
    public static StatusEntity mapRequestToEntity(StatusRequest statusRequest){
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setNameStatus(statusRequest.getNameStatus());
        statusEntity.setDescription(statusRequest.getDescription());
        return statusEntity;
    }
    public static StatusResponse mapEntityToResponse(StatusEntity statusEntity){
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setIdStatus(statusEntity.getIdStatus());
        statusResponse.setNameStatus(statusEntity.getNameStatus());
        statusResponse.setDescription(statusEntity.getDescription());
        statusResponse.setDefault(statusEntity.isDefault());
        return statusResponse;
    }
}
