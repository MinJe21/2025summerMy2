package com.example.my2.permission;

import com.example.my2.permissiondetail.Permissiondetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    Optional<Permission> findByIdAndDeletedFalse(Long id);
    List<Permission> findAllByIdAndDeletedFalse(Long permissionId);

    @Query("""
        SELECT COUNT(p.id) 
        FROM Permission p
        JOIN p.permissiondetails d
        JOIN p.permissionusers u
        WHERE d.target = :target
          AND d.func = :func
          AND u.user.id = :userId
          AND p.deleted = false
          AND d.deleted = false
          AND u.deleted = false
    """)
    Long countPermissionByTargetAndFuncAndUser(
            @Param("target") String target,
            @Param("func") Integer func,
            @Param("userId") Long userId
    );

}
