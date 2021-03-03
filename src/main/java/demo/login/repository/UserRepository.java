package demo.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.login.data.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEnrollmentNo(Long enrollmentNo);

	Optional<User> findByEmail(String email);

	Boolean existsByEnrollmentNo(Long enrollmentNo);

	Boolean existsByEmail(String email);

}
