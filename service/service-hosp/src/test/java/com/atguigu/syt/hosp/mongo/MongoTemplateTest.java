package com.atguigu.syt.hosp.mongo;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author liuzhaoxu
 * @date 2023年06月05日 19:19
 */

@SpringBootTest
public class MongoTemplateTest {

    @Resource
    private MongoTemplate mongoTemplate;

    //添加
    @Test
    public void testCreateUser(){
        User user = new User();
        user.setAge(20);
        user.setName("test");
        user.setEmail("test@qq.com");
        mongoTemplate.insert(user);
        System.out.println(user);
    }

    //查询所有
    @Test
    public void testFindUser() {
        List<User> userList = mongoTemplate.findAll(User.class);
        System.out.println(userList);
    }

    //根据id查询
    @Test
    public void testFindUserById(){
        User user = mongoTemplate.findById("6412f9b27f3f675ef4c5ebb6", User.class);
        System.out.println(user);
    }
    //修改
    @Test
    public void testUpdateUser() {
        Criteria criteria = Criteria.where("_id").is("6412f9b27f3f675ef4c5ebb6");
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("name", "zhangsan");
        update.set("age", 99);
        UpdateResult result = mongoTemplate.upsert(query, update, User.class);//改一条
        //UpdateResult result = mongoTemplate.updateMulti(query, update, User.class);//改多条
        long count = result.getModifiedCount();
        System.out.println(count);
    }

    //删除
    @Test
    public void testRemove() {
        Criteria criteria = Criteria.where("_id").is("6412f9b27f3f675ef4c5ebb6");
        Query query = new Query(criteria);
        DeleteResult result = mongoTemplate.remove(query, User.class);
        long count = result.getDeletedCount();
        System.out.println(count);
    }

    //条件查询 and
    @Test
    public void findUserList() {

        Criteria criteria = Criteria.where("name").is("test").and("age").is(20);
        Query query = new Query(criteria);

        List<User> userList = mongoTemplate.find(query, User.class);
        System.out.println(userList);
    }


    //模糊查询
    @Test
    public void findUsersLikeName() {

        Pattern pattern = Pattern.compile("^zhang", Pattern.CASE_INSENSITIVE);
        Criteria criteria = Criteria.where("name").regex(pattern);
        Query query = new Query(criteria);
        List<User> userList = mongoTemplate.find(query, User.class);
        System.out.println(userList);
    }

    //分页查询
    @Test
    public void findUsersPage() {
        Query query = new Query();
        //先查询总记录数
        long count = mongoTemplate.count(query, User.class);
        System.out.println(count);
        //后查询分页列表
        List<User> userList = mongoTemplate.find(query.skip(0).limit(2), User.class);
        System.out.println(userList);
    }
}