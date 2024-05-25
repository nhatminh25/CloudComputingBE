package com.example.todolist2n_backend.mapping;

import com.example.todolist2n_backend.model.entity.JobEntity;
import com.example.todolist2n_backend.model.request.JobRequest;
import com.example.todolist2n_backend.model.response.JobResponse;
import lombok.Data;

@Data

public class JobMapping {
    public static JobEntity mapRequestToEntit(JobRequest jobRequest) {
        JobEntity jobEntity = new JobEntity();
        jobEntity.setNameJob(jobRequest.getNameJob());
        jobEntity.setPriority(jobRequest.getPriority());
        return jobEntity;
    }

    public static JobResponse mapEntityToResponse(JobEntity jobEntity){
        JobResponse jobResponse = new JobResponse();
        jobResponse.setIdJob(jobEntity.getIdJob());
        jobResponse.setNameJob(jobEntity.getNameJob());
        jobResponse.setProgress(jobEntity.getProgress());
        jobResponse.setCreatedAt(jobEntity.getCreatedAt());
        jobResponse.setUpdatedAt(jobEntity.getUpdatedAt());
        jobResponse.setHasDetail(jobEntity.isHasDetail());
        jobResponse.setPriority(jobEntity.getPriority());
        jobResponse.setNameStatus(jobEntity.getStatus().getNameStatus());
        jobResponse.setIdStatus(jobEntity.getStatus().getIdStatus());
        jobResponse.setProgressDouble(jobEntity.getProgressDouble());
        return jobResponse;
    }
}
