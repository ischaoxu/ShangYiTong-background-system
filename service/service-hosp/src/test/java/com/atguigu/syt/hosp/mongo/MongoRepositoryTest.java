package com.atguigu.syt.hosp.mongo;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author liuzhaoxu
 * @date 2023年06月05日 18:56
 */
@SpringBootTest
public class MongoRepositoryTest {

    @Resource
    private UserRepository userRepository;

    //插入
    @Test
    public void testCreateUser(){

        User user = new User();
//        user.setId(new ObjectId());
        user.setName("小xx谷");
        user.setAge(19);
        user.setCreateDate(new Date());
        userRepository.save(user);
    }

    //查询所有
    @Test
    public void testFindAll(){
        List<User> userList = userRepository.findAll();
        System.out.println(userList);
    }
    @Test
    public void testFindByName() {
        List<User> users = userRepository.findByName("test");
        System.out.println(users);
    }

    @Test
    public void testFindByNameLike() {
        List<User> users = userRepository.findByNameLike("e");
        System.out.println(users);
    }

    @Test
    public void testFindByNameAndAge() {
        List<User> users = userRepository.findByNameAndAge("test", 20);
        System.out.println(users);
    }

//    //根据id查询
//    @Test
//    public void testFindById(){
//
//        Optional<User> optional = userRepository.findById(
//                new ObjectId()
//        );
//        boolean present = optional.isPresent();
//        if(present){
//            User user = optional.get();
//            System.out.println(user);
//        }
//    }
//
//    //条件查询
//    @Test
//    public void testFindAllExample(){
//
//        User user = new User();
//        user.setAge(19);
//        Example<User> example = Example.of(user);
//        List<User> userList = userRepository.findAll(example);
//        System.out.println(userList);
//    }
//
//    //排序查询
//    @Test
//    public void testFindAllSort(){
//        Sort sort = Sort.by(Sort.Direction.DESC, "age");
//        List<User> userList = userRepository.findAll(sort);
//        System.out.println(userList);
//    }
//
//    //分页查询
//    @Test
//    public void testFindAllPage(){
//
//        PageRequest pageRequest = PageRequest.of(0, 10);
//        Page<User> page = userRepository.findAll(pageRequest);
//        int totalPages = page.getTotalPages();
//        List<User> userList = page.getContent();
//        System.out.println(userList);
//        System.out.println(totalPages);
//    }
//
//    //更新
//    @Test
//    public void testUpdateUser(){
//
//        //注意：先查询，再更新
//        Optional<User> optional = userRepository.findById(
//                new ObjectId()
//        );
//        if(optional.isPresent()){
//            User user = optional.get();
//            user.setAge(100);
//            //user中包含id，就会执行更新
//            userRepository.save(user);
//            System.out.println(user);
//        }
//    }
//
//    //删除
//    @Test
//    public void testDeleteUser(){
//        userRepository.deleteById(
//                new ObjectId()
//        );
//    }
}