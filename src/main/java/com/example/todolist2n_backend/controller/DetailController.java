package com.example.todolist2n_backend.controller;

import com.example.todolist2n_backend.model.request.DetailRequest;
import com.example.todolist2n_backend.service.interfa.DetailService;
import com.example.todolist2n_backend.service.interfa.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RestController
@RequestMapping("/detail")

public class DetailController {
    @Autowired
    private DetailService detailService;

    @GetMapping("/get")
    public ResponseEntity<?> getDetail(
            @RequestParam Long idJob,
            @RequestParam String textSearch,
            @RequestParam String sortData,
            @RequestParam String sortType,
            @RequestParam Long limit,
            @RequestParam Long currentPage) {
        return new ResponseEntity<>(detailService.getDetail(idJob, textSearch, currentPage, limit, sortData, sortType), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDetail(@RequestBody DetailRequest detailRequest) {
        return new ResponseEntity<>(detailService.addDetail(detailRequest), HttpStatus.OK);
    }
    //    @PutMapping("/update/{id}")
    @PutMapping("/update/{idDetail}")
    public ResponseEntity<?> updateDetail(@PathVariable Long idDetail, @RequestBody DetailRequest detailRequest) {
        return new ResponseEntity<>(detailService.updateDetail(idDetail, detailRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idDetail}")
    public ResponseEntity<?> deleteDetail(
            @PathVariable Long idDetail,
            @RequestParam Long idJob,
            @RequestParam Long currentPage,
            @RequestParam Long limit) {
        return new ResponseEntity<>(detailService.deleteDetail(idDetail,idJob,currentPage,limit),HttpStatus.OK);
    }

}
