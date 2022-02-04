package com.a6raywa1cher.jsonrestsecurity.mainapp;

import com.a6raywa1cher.jsonrestsecurity.dao.repo.IUserRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends IUserRepository<User>, CrudRepository<User, Long> {

}
