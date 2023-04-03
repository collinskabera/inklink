package inklink.data;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import inklink.User;

public interface UserRepository extends CrudRepository<User, Long>{

	List<User> findByUsername(String username);
	
	@Query(nativeQuery=true, value="SELECT * FROM user WHERE role = 'ROLE_ADMIN'")
	List<User> selectAllAdmins();
}
