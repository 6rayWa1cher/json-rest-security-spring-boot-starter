package com.a6raywa1cher.websecurityspringbootstarter;

import com.a6raywa1cher.websecurityspringbootstarter.dao.repo.IUserRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends IUserRepository<User> {

}
