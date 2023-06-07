package com.atguigu.syt.hosp.mongo;


import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author liuzhaoxu
 * @date 2023年06月05日 18:55
 */

@Data
@Document("user") //指定mongodb中的集合名字
public class User {

    @Id
    private ObjectId id;
    private String name;
    private Integer age;
    private String email;
    private Date createDate;
}