package com.example.request_management.repossitory;


import com.example.request_management.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where " +
            "(:name is null or u.name like  %:name)")
    Page<User> getUsersByFilters(@Param("name") String name, Pageable pageable);
}
