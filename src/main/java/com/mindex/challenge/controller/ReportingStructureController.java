package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

@RestController
public class ReportingStructureController {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private ReportingStructureService reportingStructureService;
    
    @GetMapping("/reporting_structure/{id}")
    public ResponseEntity<? extends Object> read(@PathVariable String id) {
        LOG.debug("Received reporting structure read request for id [{}]", id);
        try {
        	ReportingStructure reportingStructure = reportingStructureService.read(id);
        	return ResponseEntity.ok(reportingStructure);
        }  catch (IllegalArgumentException e) {
        	return new ResponseEntity<>("No employee with employeeId [" + id + "] could be found", HttpStatus.NOT_FOUND);
        }
    }
}
