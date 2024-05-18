package com.ssafy.home.domain.homedeal.repository;

import com.ssafy.home.domain.homedeal.entity.HomeDeal;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomedealRepository extends JpaRepository<HomeDeal, Long> {
    //TODO : 여기 제대로 동작하나 확인해야됨!
    @Query("""
        SELECT hd
        from HomeDeal hd
        join fetch hd.home
        where hd.home.aptCode =: aptCode
        """)
    List<HomeDeal> findAllByHomeId(@Param("aptCode") Long aptCode);
}
