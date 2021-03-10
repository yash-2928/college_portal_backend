package demo.login.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.login.data.Post;
import demo.login.data.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    public List<Post> findAllByUser(User user);

}
