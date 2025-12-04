package com.config.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.config.entity.OrgInfo;

public interface OrgInfoRepository extends JpaRepository<OrgInfo, Long> {
}