package com.config.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.config.entity.OrgInfo;
import com.config.repository.OrgInfoRepository;

@Service
public class OrgInfoService {

    private final OrgInfoRepository repository;

    public OrgInfoService(OrgInfoRepository repository) {
        this.repository = repository;
    }

    public List<OrgInfo> getAll() {
        return repository.findAll();
    }
}
