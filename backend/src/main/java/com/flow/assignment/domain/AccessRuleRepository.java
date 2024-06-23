package com.flow.assignment.domain;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccessRuleRepository extends JpaRepository<AccessRule, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from AccessRule ar where ar.id = :id")
    void deleteById(@Param("id") Long id);

    Slice<AccessRule> findByContentContaining(String content, Pageable pageable);

    @Query("select ar from AccessRule ar"
            + " where (:start is null or ar.startTime <= :start)"
            + " and (:end is null or ar.endTime >= :end)")
    Slice<AccessRule> findByPermissionTime(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                           Pageable pageable);
}
