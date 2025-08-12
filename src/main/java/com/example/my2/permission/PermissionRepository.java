package com.example.my2.permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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


    @Query("SELECT p FROM Permission p " +
            "WHERE (:deleted IS NULL OR p.deleted = :deleted) " +
            "AND (:title IS NULL OR p.title LIKE %:title%) " +
            "AND (:sdate IS NULL OR p.createdAt >= :sdate) " +
            "AND (:fdate IS NULL OR p.createdAt <= :fdate)")
    Page<Permission> findBySearchConditions(
            @Param("deleted") Boolean deleted,
            @Param("title") String title,
            @Param("sdate") LocalDateTime sdate,
            @Param("fdate") LocalDateTime fdate,
            Pageable pageable
    );


    @Query("""
    SELECT COUNT(p) FROM Permission p
    WHERE (:deleted IS NULL OR p.deleted = :deleted)
      AND (:title IS NULL OR p.title LIKE %:title%)
      AND (:sdate IS NULL OR p.createdAt >= :sdate)
      AND (:fdate IS NULL OR p.createdAt <= :fdate)
""")
    long countBySearchConditions(
            @Param("deleted") Boolean deleted,
            @Param("title") String title,
            @Param("sdate") LocalDateTime  sdate,
            @Param("fdate") LocalDateTime  fdate
    );
}
