package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TTTaiXeLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TTTaiXeLogsRepository extends JpaRepository<TTTaiXeLogs, Integer> {
}
