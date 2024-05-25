package com.example.todolist2n_backend.service.impl;

import com.example.todolist2n_backend.mapping.JobMapping;
import com.example.todolist2n_backend.model.Content;
import com.example.todolist2n_backend.model.common.ResponseApi;
import com.example.todolist2n_backend.model.entity.DetailEntity;
import com.example.todolist2n_backend.model.entity.JobEntity;
import com.example.todolist2n_backend.model.entity.StatusEntity;
import com.example.todolist2n_backend.model.request.JobRequest;
import com.example.todolist2n_backend.model.response.JobResponse;
import com.example.todolist2n_backend.repository.DetailRepository;
import com.example.todolist2n_backend.repository.JobRepository;
import com.example.todolist2n_backend.repository.StatusRepository;
import com.example.todolist2n_backend.service.interfa.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.todolist2n_backend.mapping.JobMapping.mapEntityToResponse;

@Component

public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public ResponseApi getJob(String textSearch, Long currentPage, Long limit, String sortData, String sortType) {
        try {
            currentPage -= 1;
            String warning = "";
            Pageable pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
            Page<JobEntity> Page = jobRepository.searchJob(textSearch, pageable);
            List<JobResponse> listJob = Page.stream()
                    .map(JobMapping::mapEntityToResponse)
                    .collect(Collectors.toList());
            Integer totalJob = Math.toIntExact(Page.getTotalElements());
            Integer totalPageJob = Math.toIntExact(Page.getTotalPages());
            if (currentPage.intValue() > totalPageJob) {
                currentPage = totalPageJob.longValue();
                pageable = PageRequest.of(currentPage.intValue(), limit.intValue(), Sort.by(sortOrder(sortData, sortType)));
                Page = jobRepository.searchJob(textSearch, pageable);
                listJob = Page.stream()
                        .map(JobMapping::mapEntityToResponse)
                        .collect(Collectors.toList());
                totalJob = Math.toIntExact(Page.getTotalElements());
                warning = "Trang không tồn tại";
            }
            Content content = new Content();
            content.setWarning(warning);
            content.setList(listJob);
            content.setCurrentPage(currentPage.intValue() + 1);
            content.setTotalRecord(totalJob);
            return new ResponseApi("Thành công", content, true);
        } catch (Exception e) {
            return new ResponseApi(e.getMessage(), null, false);
        }
    }

    @Override
    public ResponseApi addJob(JobRequest jobRequest) {
        try {
            Content content = new Content();
            String warning = "";
            JobEntity jobEntity = JobMapping.mapRequestToEntit(jobRequest);
            Optional<StatusEntity> itemStatus = statusRepository.findById(jobRequest.getIdStatus());
            if (!itemStatus.isPresent()) {
                warning = "Trạng thái không tồn tại";
                content.setWarning(warning);
                content.setList(null);
                content.setCurrentPage(null);
                content.setTotalRecord(null);
                return new ResponseApi("Thêm công việc thất bại", content, false);
            } else {
                Date createdAt = new Date();
                jobEntity.setCreatedAt(createdAt);
                jobEntity.setUpdatedAt(createdAt);
                jobEntity.setStatus(statusRepository.getById(jobRequest.getIdStatus()));
                jobEntity.setHasDetail(false);
                jobEntity.setProgressDouble(0);
                jobEntity.setProgress(0 + "/" + 0);
                jobRepository.save(jobEntity);
                if (jobRequest.getNameStatus().equals(itemStatus.get().getNameStatus()) == false) {
                    warning = "Trạng thái đã bị thay đổi";
                }
            }
            content.setWarning(warning);
            content.setList(null);
            content.setCurrentPage(null);
            content.setTotalRecord(null);
            return new ResponseApi("Thêm công việc thành công", content, true);
        } catch (Exception e) {
            return new ResponseApi(e.getMessage(), null, false);
        }
    }

    @Override
    public ResponseApi updateJob(Long id, JobRequest jobRequest) {
        try {
            Content content = new Content();
            String warning = "";
            JobEntity jobEntity = JobMapping.mapRequestToEntit(jobRequest);
            Optional<JobEntity> jobUpdate = jobRepository.findById(id);
            Optional<StatusEntity> itemStatus = statusRepository.findById(jobRequest.getIdStatus());
            if (!jobUpdate.isPresent()) {
                warning = "Công việc không tồn tại";
                content.setWarning(warning);
                content.setList(null);
                content.setCurrentPage(null);
                content.setTotalRecord(null);
                return new ResponseApi("Sửa công việc thất bại", content, false);
            } else {
                if (!itemStatus.isPresent()) {
                    warning = "Trạng thái không tồn tại";
                    content.setWarning(warning);
                    content.setList(null);
                    content.setCurrentPage(null);
                    content.setTotalRecord(null);
                    return new ResponseApi("Sửa công việc thất bại", content, false);
                } else if (jobRequest.getNameStatus().equals(itemStatus.get().getNameStatus()) == false) {
                    warning = "Trạng thái đã bị thay đổi";
                }
                Date updatedAt = new Date();
                jobEntity.setUpdatedAt(updatedAt);

                Optional<JobEntity> jobEntitySave = jobRepository.findById(id);
                jobEntitySave.get().setNameJob(jobEntity.getNameJob());
                jobEntitySave.get().setUpdatedAt(updatedAt);
                jobEntitySave.get().setPriority(jobEntity.getPriority());
                jobEntitySave.get().setStatus(statusRepository.getById(jobRequest.getIdStatus()));
                jobRepository.save(jobEntitySave.get());
                List <JobResponse> listJob = new ArrayList<>();
                JobResponse jobResponse = JobMapping.mapEntityToResponse(jobEntitySave.get());
                listJob.add(jobResponse);
                content.setList(listJob);
                content.setWarning(warning);
                content.setCurrentPage(null);
                content.setTotalRecord(null);
                return new ResponseApi("Sửa công việc thành công", content, true);
            }

        } catch (Exception e) {
            return new ResponseApi(e.getMessage(), null, false);
        }

    }

    @Override
    public ResponseApi deleteJob(Long id, Long limit, Long currentPage) {
        try {
            String warning = "";
            Optional<JobEntity> jobEntity = jobRepository.findById(id);
            if (jobEntity.isEmpty()) {
                warning = "Công việc không tồn tại";
                Content content = new Content();
                content.setWarning(warning);
                content.setCurrentPage(currentPage.intValue() + 1);
                return new ResponseApi("Xóa công việc thất bại", content, false);
            }

            jobRepository.deleteById(id);
            long total = jobRepository.count();
            if(total == 0 ) {
                total += 1;
            }
            long totalPage = (int) (Math.ceil((int) total / (double) limit));
            System.out.println(total);
            currentPage = Math.min(currentPage, totalPage);

            Content content = new Content();
            content.setWarning("");
            content.setCurrentPage(currentPage.intValue());
            return new ResponseApi("Xóa công việc thành công", content, true);
        } catch (Exception e) {
            return new ResponseApi(e.getMessage(), null, false);
        }
    }

    public List<Sort.Order> sortOrder(String sort, String sortDirection) {
        System.out.println(sortDirection);
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        if (sortDirection != null) {
            direction = Sort.Direction.fromString(sortDirection);
        } else {
            direction = Sort.Direction.DESC;
        }
        sorts.add(new Sort.Order(direction, sort));
        return sorts;
    }
}

