package com.example.todolist2n_backend.service.impl;

import com.example.todolist2n_backend.mapping.DetailMapping;
import com.example.todolist2n_backend.model.Content;
import com.example.todolist2n_backend.model.common.ResponseApi;
import com.example.todolist2n_backend.model.entity.DetailEntity;
import com.example.todolist2n_backend.model.entity.JobEntity;
import com.example.todolist2n_backend.model.entity.StatusEntity;
import com.example.todolist2n_backend.model.request.DetailRequest;
import com.example.todolist2n_backend.model.response.DetailResponse;
import com.example.todolist2n_backend.repository.DetailRepository;
import com.example.todolist2n_backend.repository.JobRepository;
import com.example.todolist2n_backend.repository.StatusRepository;
import com.example.todolist2n_backend.service.interfa.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component

public class DetailServiceImpl implements DetailService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public ResponseApi getDetail(Long idJob, String textSearch, Long currentPage, Long limit, String sortData, String sortType) {
        try{
            currentPage-=1;
            Pageable pageableDetail = PageRequest.of(currentPage.intValue(), limit.intValue(),Sort.by(sortOrder(sortData,sortType)));
            String warning = "";
            Optional<JobEntity> itemJob = jobRepository.findById(idJob);
            Content content = new Content();
            if (itemJob.isEmpty()){
                warning = "Công việc không tồn tại";
                content.setTotalRecord(null);
                content.setList(null);
                content.setCurrentPage(1);
                content.setWarning(warning);
                return new ResponseApi("Công việc không tồn tại", null, false );
            }else{
                Integer totalDetail;
                Integer totalPageDetail;
                Page <DetailEntity> Page = detailRepository.searchDetail(textSearch,idJob,pageableDetail);
                List <DetailResponse> listDetail = Page.stream()
                        .map(DetailMapping::mapEntityToResponse)
                        .collect(Collectors.toList());
                totalDetail = Math.toIntExact(Page.getTotalElements());
                totalPageDetail = Math.toIntExact(Page.getTotalPages());
                if (currentPage.intValue() > totalPageDetail){
                    currentPage = totalPageDetail.longValue();
                    pageableDetail =  PageRequest.of(currentPage.intValue(),limit.intValue(), Sort.by(sortOrder(sortData,sortType)));
                    Page = detailRepository.searchDetail(textSearch,idJob,pageableDetail);
                    listDetail = Page.stream()
                            .map(DetailMapping::mapEntityToResponse)
                            .collect(Collectors.toList());
                    totalDetail = Math.toIntExact(Page.getTotalElements());
                    warning = "Trang không tồn tại";
                }
                content.setWarning(warning);
                content.setList(listDetail);
                content.setCurrentPage(currentPage.intValue() + 1);
                content.setTotalRecord(totalDetail);
                return new ResponseApi("Thành công",content,true);
            }

        }
        catch (Exception e){
            return new ResponseApi(e.getMessage(), null, false);
        }

    }

    @Override
    public ResponseApi addDetail(DetailRequest detailRequest) {

        try {
            Content content = new Content();
            String warning = "";
            Optional<JobEntity> jobEntity = jobRepository.findById(detailRequest.getIdJob());
            if (!jobEntity.isPresent()) {
                warning = "Công việc không tồn tại";
                content.setTotalRecord(null);
                content.setList(null);
                content.setCurrentPage(1);
                content.setWarning(warning);
                return new ResponseApi("Thất bại", content, false);
            } else {
                DetailEntity detailEntity = DetailMapping.mapRequestToEntity(detailRequest);
                Optional<StatusEntity> itemStatus = statusRepository.findById(detailRequest.getIdStatus());
                if (!itemStatus.isPresent()) {
                    warning = "Trạng thái không tồn tại";
                    content.setWarning(warning);
                    return new ResponseApi("Thất bại", content, false);
                } else {
                    Date createAt = new Date();
                    detailEntity.setCreatedAt(createAt);
                    detailEntity.setUpdatedAt(createAt);
                    detailEntity.setStatus(statusRepository.getById(detailRequest.getIdStatus()));
                    detailEntity.setJob(jobRepository.getById(detailRequest.getIdJob()));
                    detailRepository.save(detailEntity);
                    if (detailRequest.getNameStatus().equals(itemStatus.get().getNameStatus())==false) {
                        warning = "Trạng thái đã bị thay đổi";
                        content.setWarning(warning);
                    }
                    Date date = new Date();
                    Optional<JobEntity> itemJob = jobRepository.findById(detailRequest.getIdJob());
                    JobEntity itemJobParent = itemJob.get();
                    itemJobParent.setUpdatedAt(date);

                    int totalDetail = detailRepository.findByIdJob(detailRequest.getIdJob()).size();
                    int totalDetailSuccess = detailRepository.findByIdJobAndStatus(detailRequest.getIdJob(),2l).size();
                    itemJobParent.setProgress(totalDetailSuccess + "/" + totalDetail);
                    itemJobParent.setProgressDouble(((double)totalDetailSuccess/(double)totalDetail)*100);
                    itemJobParent.setHasDetail(true);
                    itemJobParent.setStatus(statusRepository.findById(statusMathJob(totalDetailSuccess,totalDetail)).get());
                    jobRepository.save(itemJobParent);
                    content.setCurrentPage(1);
                    content.setTotalRecord(totalDetail);
                    return new ResponseApi("Thêm chi tiết công việc thành công", content, true);
                }

            }
        } catch (Exception e) {
            return new ResponseApi(e.getMessage(), null, false);
        }
    }

    @Override
    public ResponseApi updateDetail(Long idDetail, DetailRequest detailRequest) {
        try {
            Content content = new Content();
            String warning = "";
            Optional<JobEntity> jobEntity = jobRepository.findById(detailRequest.getIdJob());
            if (!jobEntity.isPresent()) {
                warning = "Công việc không tồn tại";
                content.setTotalRecord(null);
                content.setList(null);
                content.setCurrentPage(1);
                content.setWarning(warning);
                return new ResponseApi("Thất bại", content, false);
            } else {
                DetailEntity detailEntity = DetailMapping.mapRequestToEntity(detailRequest);
                Optional<DetailEntity> itemDetail = detailRepository.findById(idDetail);
                if (!itemDetail.isPresent()) {
                    warning = "Chi tiết công việc không tồn tại";
                    content.setCurrentPage(null);
                    content.setList(null);
                    content.setWarning(warning);
                    content.setTotalRecord(null);
                    return new ResponseApi("Sửa chi tiết công việc thất bại", content, false);
                } else {
                    Optional<StatusEntity> itemStatus = statusRepository.findById(detailRequest.getIdStatus());
                    if (!itemStatus.isPresent()) {
                        warning = "Trạng thái không tồn tại";
                        content.setWarning(warning);
                        return new ResponseApi("Sửa chi tiết công việc thất bại", content, false);
                    } else {
                        JobEntity jobEntityParent = jobRepository.findById(detailRequest.getIdJob()).get();
                        Date updatedAt = new Date();
                        detailEntity.setCreatedAt(itemDetail.get().getCreatedAt());
                        detailEntity.setUpdatedAt(updatedAt);
                        detailEntity.setStatus(statusRepository.getById(detailRequest.getIdStatus()));
                        detailEntity.setJob(jobRepository.getById(detailRequest.getIdJob()));
                        detailEntity.setIdDetail(idDetail);
                        detailRepository.save(detailEntity);
                        Date date = new Date();
                        Optional<JobEntity> itemJob = jobRepository.findById(detailRequest.getIdJob());

                        JobEntity itemJobParent = itemJob.get();
                        itemJobParent.setUpdatedAt(date);

                        int totalDetail = detailRepository.findByIdJob(detailRequest.getIdJob()).size();
                        int totalDetailSuccess = detailRepository.findByIdJobAndStatus(detailRequest.getIdJob(),2l).size();
                        itemJobParent.setProgress(totalDetailSuccess + "/" + totalDetail);
                        itemJobParent.setProgressDouble(((double)totalDetailSuccess/(double)totalDetail)*100);
                        itemJobParent.setHasDetail(true);
                        itemJobParent.setStatus(statusRepository.findById(statusMathJob(totalDetailSuccess,totalDetail)).get());
                        jobRepository.save(itemJobParent);
                        if (detailRequest.getNameStatus().equals( itemStatus.get().getNameStatus())==false) {
                            warning = "Trạng thái đã bị thay đổi";
                            content.setWarning(warning);
                        }
                        List<DetailResponse> listDetailResponse = new ArrayList<>();
                        listDetailResponse.add(DetailMapping.mapEntityToResponse(detailEntity));
                        content.setList(listDetailResponse);
                        content.setCurrentPage(null);
                        content.setTotalRecord(null);
                        return new ResponseApi("Sửa chi tiết công việc thành công", content, true);
                    }
                }
            }
        } catch (Exception e) {
            return new ResponseApi(e.getMessage(), null, false);
        }
    }

    @Override
    public ResponseApi deleteDetail(Long idDetail, Long idJob, Long currentPage, Long limit) {
        try{
            Content content = new Content();
            String warning= "";
            Optional<JobEntity> itemJob = jobRepository.findById(idJob);

            if (itemJob.isEmpty()){

                warning = "Công việc không tồn tại!";
                content.setTotalRecord(null);
                content.setList(null);
                content.setCurrentPage(1);
                content.setWarning(warning);
                return new ResponseApi("Thất bại",content, false );
            }else{

                Optional <DetailEntity> itemDetail = detailRepository.findById(idDetail);
                if (itemDetail.isEmpty()){

                    warning = "Chi tiết công việc không tồn tại";
                    content.setCurrentPage(currentPage.intValue()+1);
                    content.setList(null);
                    content.setWarning(warning);
                    content.setTotalRecord(null);
                    return new ResponseApi("Thất bại", content, false);

                }else {
                    detailRepository.deleteById(idDetail);
                    List <DetailEntity> listDetail = detailRepository.findByIdJob(idJob);
                    Date date = new Date();
                    JobEntity itemJobSet = itemJob.get();
                    itemJobSet.setUpdatedAt(date);
                    int totalDetail = listDetail.size();

                    if (totalDetail > 0) {
                        List<DetailEntity> listDetailSucces = detailRepository.findByIdJobAndStatus(idJob, 2L);
                        int totalDetailSucces = listDetailSucces.size();
                        double progressDouble = totalDetailSucces / totalDetail;
                        String progress = totalDetailSucces + "/" + totalDetail;
                        boolean hasDetail = true;
                        itemJobSet.setStatus(statusRepository.findById(statusMathJob(totalDetailSucces,totalDetail)).get());
                        itemJobSet.setProgressDouble(progressDouble*100);
                        itemJobSet.setProgress(progress);
                        itemJobSet.setHasDetail(hasDetail);
                        Integer totalPageDetail = (int) Math.ceil((double) totalDetail/ limit.intValue());
                        if (totalPageDetail < currentPage.intValue()){
                            Integer currentPageBe = totalPageDetail;
                            content.setCurrentPage(currentPageBe);

                        }else {
                            Integer currentPageBe = currentPage.intValue();
                            content.setCurrentPage(currentPageBe);
                        }
                    } else {
                        double progressDouble = 0;
                        String progress = 0 + "/" + 0;
                        boolean hasDetail = false;
                        itemJobSet.setProgressDouble(progressDouble*100);
                        itemJobSet.setProgress(progress);
                        itemJobSet.setHasDetail(hasDetail);
                        itemJobSet.setStatus(statusRepository.findById(1L).get());
                        Integer currentPageBe = 1;
                        content.setCurrentPage(currentPageBe);
                    }
                    content.setTotalRecord(null);
                    content.setList(null);
                    content.setWarning(warning);
                    jobRepository.save(itemJobSet);
                    return new ResponseApi ("Xóa chi tiết công việc thành công", content, true);
                }
            }
        } catch (Exception e) {
            return new ResponseApi(e.getMessage(),null,false);
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

    public Long statusMathJob (int totalDetailSucces, int totalDetail) {
        int statusMath = totalDetailSucces/totalDetail;
        if (statusMath == 1) {
            return 2L;
        }
        return 1L;
    }
}
