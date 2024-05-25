package com.example.todolist2n_backend.controller;

import com.example.todolist2n_backend.model.request.JobRequest;
import com.example.todolist2n_backend.service.interfa.JobService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin
@RestController
@RequestMapping("/job")

public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping("/get")
    public ResponseEntity<?> getJob(@RequestParam String textSearch,
                                    @RequestParam String sortData,
                                    @RequestParam String sortType,
                                    @RequestParam Long limit,
                                    @RequestParam Long currentPage) {
        return new ResponseEntity<>(jobService.getJob(textSearch, currentPage, limit, sortData, sortType), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addJob(@RequestBody JobRequest jobRequest) {
        return new ResponseEntity<>(jobService.addJob(jobRequest), HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody JobRequest jobRequest) {
        return new ResponseEntity<>(jobService.updateJob(id, jobRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id, @RequestParam Long limit, @RequestParam Long currentPage) {
        return new ResponseEntity<>(jobService.deleteJob(id, limit, currentPage), HttpStatus.OK);
    }

//    @DeleteMapping("/delete/{id}")

}
