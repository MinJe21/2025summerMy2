package com.example.my2.permissiondetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissiondetailRepository extends JpaRepository<Permissiondetail,Long> {
    Permissiondetail findByPermissionIdAndTargetAndFunc(Long permissionId, String target, Integer func);
    List<Permissiondetail> findAllByPermissionIdAndDeletedFalse(Long permissionId);
    Optional<Permissiondetail> findByIdAndDeletedFalse(Long permissionId);
}
