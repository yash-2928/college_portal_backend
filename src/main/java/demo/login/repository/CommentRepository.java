package demo.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.login.data.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
}
