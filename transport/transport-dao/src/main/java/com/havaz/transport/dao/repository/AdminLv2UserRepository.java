package com.havaz.transport.dao.repository;

import com.havaz.transport.core.constant.UserRole;
import com.havaz.transport.dao.entity.AdminLv2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminLv2UserRepository extends JpaRepository<AdminLv2UserEntity, Integer> {
    //    @Query("SELECT a FROM AdminLv2UserEntity a ")
//    public List<AdminLv2UserEntity> getTaiXeTrungChuyen();
    List<AdminLv2UserEntity> getAllByAdmIdIn(Set<Integer> admId);

    List<AdminLv2UserEntity> findAllByAdmIdIn(Set<Integer> admId);

    @Query("SELECT adm FROM AdminLv2UserEntity adm LEFT JOIN AdminLv2UserGroupIdEntity adg ON adg.admgAdminId = adm.admId " +
                   " WHERE adm.admActive =:admActive AND (adm.admCtv =:admCtv OR adg.admgGroupId =:isLx)")
    List<AdminLv2UserEntity> findAllByCtvOrLxTc(@Param("admActive") Boolean admActive,
                                                @Param("admCtv") Integer admCtv,
                                                @Param("isLx") Integer isLx);

    AdminLv2UserEntity findTopByAdmLoginnameEquals(String username);

    @Query("SELECT ad.admName FROM AdminLv2UserEntity ad WHERE ad.admId = ?1")
    String findAdminNameById(int id);

    @Query("SELECT u FROM AdminLv2UserEntity u LEFT JOIN u.roles r WHERE r.name IS NULL OR r.name <> :userRole GROUP BY u.admId ")
    List<AdminLv2UserEntity> findUsersIsNotRole(@Param("userRole") UserRole userRole);
}
