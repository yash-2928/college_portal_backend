package demo.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.login.data.JobReport;

@Repository
public interface JobReportRepository extends JpaRepository<JobReport, Long>{
    
}
