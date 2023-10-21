package com.mindex.challenge.controller;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);
    
    @Autowired
    private CompensationService compensationService;
    
    @PostMapping("/compensation")
    public ResponseEntity<? extends Object> create(@Valid @RequestBody Compensation compensation) {
        LOG.debug("Received Compensation create request for [{}]", compensation);

        try {
            Compensation comp = compensationService.create(compensation);
            return new ResponseEntity<Compensation>(comp, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
        	return new ResponseEntity<>("No employee with employeeId [" + compensation.getEmployee().getEmployeeId() + "] could be found", HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
        	return new ResponseEntity<>("Compensation for employee with employeeId ["  + compensation.getEmployee().getEmployeeId() + "] already exists", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/compensation/{id}")
    public ResponseEntity<? extends Object> read(@PathVariable String id) {
        LOG.debug("Received Compensation read request for employeeId [{}]", id);

        try {
        	Compensation comp = compensationService.read(id);
        	return ResponseEntity.ok(comp);
        } catch (IllegalArgumentException e) {
        	return new ResponseEntity<>("No compensation for employee with employeeId [" + id + "] could be found", HttpStatus.NOT_FOUND);
        }
    }

}
