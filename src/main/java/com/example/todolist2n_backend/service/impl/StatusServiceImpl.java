package com.example.todolist2n_backend.service.impl;

import com.example.todolist2n_backend.mapping.JobMapping;
import com.example.todolist2n_backend.mapping.StatusMapping;
import com.example.todolist2n_backend.model.Content;
import com.example.todolist2n_backend.model.common.ResponseApi;
import com.example.todolist2n_backend.model.entity.DetailEntity;
import com.example.todolist2n_backend.model.entity.JobEntity;
import com.example.todolist2n_backend.model.entity.StatusEntity;
import com.example.todolist2n_backend.model.request.StatusRequest;
import com.example.todolist2n_backend.model.response.JobResponse;
import com.example.todolist2n_backend.model.response.StatusResponse;
import com.example.todolist2n_backend.repository.DetailRepository;
import com.example.todolist2n_backend.repository.JobRepository;
import com.example.todolist2n_backend.repository.StatusRepository;
import com.example.todolist2n_backend.service.interfa.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component

public class StatusServiceImpl implements StatusService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public ResponseApi getStatus(String textSearch, Long currentPage, Long limit, String sortData, String sortType) {
        try{
            currentPage -=1;
            String warning = "";
            Pageable pageable =  PageRequest.of(currentPage.intValue(),limit.intValue(), Sort.by(sortOrder(sortData,sortType)));
            Page<StatusEntity> Page = statusRepository.searchStatus(textSearch, pageable);
            List<StatusResponse> listStatus = Page.stream()
                    .map(StatusMapping::mapEntityToResponse)
                    .collect(Collectors.toList());
            Integer totalStatus = Math.toIntExact(Page.getTotalElements());
            Integer totalPageStatus = Math.toIntExact(Page.getTotalPages());
            if (currentPage.intValue() > totalPageStatus){
                currentPage = totalPageStatus.longValue();
                pageable =  PageRequest.of(currentPage.intValue(),limit.intValue(), Sort.by(sortOrder(sortData,sortType)));
                Page = statusRepository.searchStatus(textSearch, pageable);
                listStatus = Page.stream()
                        .map(StatusMapping::mapEntityToResponse)
                        .collect(Collectors.toList());
                totalStatus = Math.toIntExact(Page.getTotalElements());
                warning = "Trang không tồn tại!";
            }
            Content content = new Content();
            content.setWarning(warning);
            content.setList(listStatus);
            content.setCurrentPage(currentPage.intValue() + 1);
            content.setTotalRecord(totalStatus);
            return new ResponseApi("Thành công",content,true);
        }

        catch(Exception e){
            return new ResponseApi(e.getMessage(),null, false );
        }
    }

    @Override
    public ResponseApi addStatus(StatusRequest statusRequest, Long limit) {
        try {
            StatusEntity statusEntity = StatusMapping.mapRequestToEntity(statusRequest);
            statusEntity.setDefault(false);
            statusRepository.save(statusEntity);
            int total = statusRepository.findAll().size();
            int totalPage = (int) (Math.ceil(total / limit)+1);
            int currentPage = totalPage;
            Content content = new Content();
            content.setList(null);
            content.setTotalRecord(total);
            content.setCurrentPage(currentPage);
            content.setWarning("");
            return new ResponseApi("Thêm trạng thái thành công", content, true);
        }catch (Exception e){
            return new ResponseApi(e.getMessage(), null, false);
        }
    }

    @Override
    public ResponseApi updateStatus(Long idStatus, StatusRequest statusRequest) {
        try {
            Content content =new Content();
            Optional<StatusEntity> statusEntityOptional = statusRepository.findById(idStatus);
            StatusEntity statusEntityResquest = StatusMapping.mapRequestToEntity(statusRequest);
            if (!statusEntityOptional.isPresent()){
                content.setWarning("Trạng thái không tồn tại");
                return new ResponseApi("Sửa trạng thái thất bại", content, false);
            }else {
                StatusEntity statusEntity = statusEntityOptional.get();
                if (statusEntity.isDefault() == true) {
                    content.setWarning("Không thể sửa trạng thái mặc định");
                    return new ResponseApi("Sửa trạng thái thất bại", content, false);
                } else {
                    statusEntity.setNameStatus(statusEntityResquest.getNameStatus());
                    statusEntity.setDescription(statusEntityResquest.getDescription());

                    List<JobEntity> listJob = jobRepository.getListJoblbyStatus(idStatus);
                    List<DetailEntity> listDetail = detailRepository.getListDetailbyStatus(idStatus);

                    if (listJob.size() == 0 & listDetail.size() == 0) {
                        statusRepository.save(statusEntity);
                        return new ResponseApi("Sửa trạng thái thành công", null, true);
                    } else {
                        statusRepository.save(statusEntity);
                        Date date = new Date();
                        for (JobEntity jobEntity : listJob) {
                            jobEntity.setUpdatedAt(date);
                        }
                        for (DetailEntity detailEntity : listDetail) {
                            detailEntity.setUpdatedAt(date);
                            Long idJobOfDetailEntity = detailEntity.getJob().getIdJob();
                            JobEntity jobEntityParent = jobRepository.findById(idJobOfDetailEntity).get();
                            jobEntityParent.setUpdatedAt(date);
                            jobRepository.save(jobEntityParent);
                        }
                        return new ResponseApi("Sửa trạng thái thành công", null, true);
                    }
                }
            }
        }catch (Exception e){
            return new ResponseApi(e.getMessage(),null,false);
        }
    }

    @Override
    public ResponseApi deleteStatus(Long idStatus, Long limit, Long currentPage) {
        Content content = new Content();
        try {
            String warning = "";
            StatusEntity statusEntity = statusRepository.findById(idStatus).orElse(null);
            if(statusEntity == null){
                warning = "Trạng thái không tồn tại!";
                content.setWarning(warning);
                return new ResponseApi("Xóa trạng thái thất bại", content, false);
            } else {
                if(statusEntity.isDefault() == false){
                    List<DetailEntity> listDetailByStatus = detailRepository.getListDetailbyStatus(idStatus);
                    List<JobEntity> listJobByStatus = jobRepository.getListJoblbyStatus(idStatus);
                    if(listDetailByStatus.size() == 0 && listJobByStatus.size() == 0){
                        statusRepository.deleteById(idStatus);
                        Integer totalStatus = statusRepository.findAll().size();
                        Integer totalPageStatus = (int)Math.ceil(totalStatus.doubleValue()/ limit.doubleValue());
                        content.setList(null);
                        content.setTotalRecord(totalStatus);
                        if(currentPage <= totalPageStatus){
                            content.setCurrentPage(currentPage.intValue());
                        } else {
                            currentPage = totalPageStatus.longValue();
                            content.setCurrentPage(currentPage.intValue());
                        }
                        return new ResponseApi("Xóa trạng thái thành công", content, true);
                    }else {
                        for (DetailEntity detailEntity : listDetailByStatus) {
                            detailEntity.setStatus(statusRepository.getById(1L));
                            detailRepository.save(detailEntity);
                        }
                        for (JobEntity jobEntity : listJobByStatus){
                            jobEntity.setStatus(statusRepository.getById(1L));
                            jobRepository.save(jobEntity);
                        }
                        statusRepository.deleteById(idStatus);
                        Integer totalStatus = statusRepository.findAll().size();
                        Integer totalPageStatus = (int)Math.ceil(totalStatus.doubleValue()/ limit.doubleValue());
                        content.setList(null);
                        content.setTotalRecord(totalStatus);
                        if(currentPage <= totalPageStatus){
                            content.setCurrentPage(currentPage.intValue());
                        } else {
                            currentPage = totalPageStatus.longValue();
                            content.setCurrentPage(currentPage.intValue());
                        }
                        return new ResponseApi("Xóa trạng thái thành công", null, true);
                    }
                } else {
                    content.setWarning("Không thể xóa trạng thái mặc định");
                    return new ResponseApi("Xóa trạng thái thất bại", content, false);
                }
            }
        }
        catch (Exception e){
            return new ResponseApi(e.getMessage(), content, false);
        }
    }

    @Override
    public ResponseApi getAllStatus() {
        try{
            List<StatusEntity> entityList = statusRepository.findAll();
            List<StatusResponse> listStatus = entityList.stream()
                    .map(StatusMapping::mapEntityToResponse)
                    .collect(Collectors.toList());
            Content content = new Content();
            content.setList(listStatus);
            content.setWarning("");
            content.setCurrentPage(null);
            content.setTotalRecord(null);

            return new ResponseApi("Lấy dữ liệu thành công", content,true);
        }catch (Exception e){
            return new ResponseApi(e.getMessage(), null,false);
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

