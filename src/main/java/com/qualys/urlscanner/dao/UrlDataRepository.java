package com.qualys.urlscanner.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qualys.urlscanner.entity.UrlDataEntity;

@Repository
public interface UrlDataRepository extends JpaRepository<UrlDataEntity, Integer> {
	
	UrlDataEntity findByUrl(String url);
	
	@Query(value ="SELECT * from url_scan_details order by submitted_on desc limit 10" , nativeQuery = true)
	List<UrlDataEntity> findLast10ByOrderBySubmittedOnDesc();
	UrlDataEntity findByUrlId(Integer urlId);
	@Query("SELECT e FROM UrlDataEntity e WHERE e.SubmittedOn >= :sdate AND e.SubmittedOn <= :edate")
	List<UrlDataEntity> findDateBetweenRecords(@Param("sdate") @Temporal(TemporalType.TIMESTAMP)Date sdate,@Param("edate") @Temporal(TemporalType.TIMESTAMP)Date edate);

}
