package demo.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.login.data.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {
    
}
