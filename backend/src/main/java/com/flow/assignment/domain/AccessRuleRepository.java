package com.flow.assignment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccessRuleRepository extends JpaRepository<AccessRule, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from AccessRule ar where ar.id = :id")
    void deleteById(@Param("id") Long id);
}
