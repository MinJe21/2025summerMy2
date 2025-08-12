package com.example.my2.permissionuser;

import com.example.my2.permissiondetail.Permissiondetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionuserRepository extends JpaRepository<Permissionuser,Long> {
    Permissionuser findByPermissionIdAndUserId(Long permissionId, Long userId);
    Optional<Permissionuser> findByIdAndDeletedFalse(Long permissionId);
    List<Permissionuser> findAllByPermissionIdAndDeletedFalse(Long permissionId);

    @Query("SELECT pu FROM Permissionuser pu " +
            "JOIN FETCH pu.user " +
            "JOIN FETCH pu.permission " +
            "WHERE pu.permission.id = :permissionId")
    List<Permissionuser> findByPermissionIdWithUser(@Param("permissionId") Long permissionId);

}

