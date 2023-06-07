package com.atguigu.syt.hosp.mongo;

import cn.hutool.core.lang.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    List<User> findByName(String name);
    List<User> findByNameLike(String name);
    List<User> findByNameAndAge(String name, Integer age);
}