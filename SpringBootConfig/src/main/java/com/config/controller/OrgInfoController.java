package com.config.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import com.config.entity.OrgInfo;
import com.config.service.OrgInfoService;

public class OrgInfoController {
	private final OrgInfoService service;

    public OrgInfoController(OrgInfoService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<OrgInfo> getAll() {
        return service.getAll();
    }
}
