package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.LenhConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LenhConfigRepository extends JpaRepository<LenhConfigEntity,Integer> {

    @Query(value = "SELECT * FROM tc_lenh_config u ORDER BY u.last_updated_at DESC",nativeQuery = true)
    LenhConfigEntity findStatus();

}
