package com.example.my2.user;

import com.example.my2.permission.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
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

    @Query("""
    SELECT u FROM User u
    WHERE (:deleted IS NULL OR u.deleted = :deleted)
      AND (:name IS NULL OR u.name LIKE %:name%)
      AND (:sdate IS NULL OR u.createdAt >= :sdate)
      AND (:fdate IS NULL OR u.createdAt <= :fdate)
""")
    Page<User> findBySearchConditions(
            @Param("deleted") Boolean deleted,
            @Param("name") String name,
            @Param("sdate") LocalDateTime sdate,
            @Param("fdate") LocalDateTime fdate,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(u) FROM User u
    WHERE (:deleted IS NULL OR u.deleted = :deleted)
      AND (:name IS NULL OR u.name LIKE %:name%)
      AND (:sdate IS NULL OR u.createdAt >= :sdate)
      AND (:fdate IS NULL OR u.createdAt <= :fdate)
""")
    long countBySearchConditions(
            @Param("deleted") Boolean deleted,
            @Param("name") String name,
            @Param("sdate") LocalDateTime sdate,
            @Param("fdate") LocalDateTime fdate
    );


}
