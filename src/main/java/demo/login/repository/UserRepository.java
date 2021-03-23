package demo.login.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.login.data.Role;
import demo.login.data.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEnrollmentNo(Long enrollmentNo);

	Optional<User> findByEmail(String email);

	Boolean existsByEnrollmentNo(Long enrollmentNo);

	Boolean existsByEmail(String email);

	List<User> findAllByRolesIn(Set<Role> roles);

	
}
