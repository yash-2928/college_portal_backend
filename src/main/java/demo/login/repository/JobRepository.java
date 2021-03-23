package demo.login.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.login.data.Job;
import demo.login.data.User;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    public List<Job> findAllByUser(User user);
    

}
