package com.example.todolist2n_backend.controller;

import com.example.todolist2n_backend.model.common.ResponseApi;
import com.example.todolist2n_backend.model.request.StatusRequest;
import com.example.todolist2n_backend.service.interfa.StatusService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RestController
@RequestMapping("/status")

public class StatusController {
    @Autowired
    private StatusService statusService;

    @GetMapping("/get")
    public ResponseEntity<?> getStatus(@RequestParam String textSearch,
                                       @RequestParam Long currentPage,
                                       @RequestParam Long limit,
                                       @RequestParam String sortData,
                                       @RequestParam String sortType) {
        return new ResponseEntity<>(statusService.getStatus(textSearch, currentPage, limit, sortData, sortType), HttpStatus.OK);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStatus() {
        return new ResponseEntity<>(statusService.getAllStatus(), HttpStatus.OK);
    }


    @PostMapping("/add")
    public ResponseEntity<?> addStatus(@RequestBody StatusRequest statusRequest,
                                       @RequestParam Long limit) {
        return new ResponseEntity<>(statusService.addStatus(statusRequest, limit), HttpStatus.OK);
    }

    @PutMapping("/update/{idStatus}")
    public ResponseEntity<?> updateStatus(@PathVariable Long idStatus,
                                          @RequestBody StatusRequest statusRequest) {
        return new ResponseEntity<>(statusService.updateStatus(idStatus, statusRequest), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idStatus}")
    public ResponseEntity<?> deleteStatus(@PathVariable Long idStatus,
                                          @PathParam("currentPage") Long currentPage,
                                          @PathParam("limit") Long limit) {
        return new ResponseEntity<>(statusService.deleteStatus(idStatus, currentPage, limit), HttpStatus.OK);
    }
}
