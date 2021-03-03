package demo.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.login.data.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
}
