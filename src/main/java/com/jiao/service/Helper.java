package com.jiao.service;

import com.jiao.Person;
import com.jiao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class Helper {

    @Autowired
    PersonRepository personRepository;

    public void save4() {

        Person person4 = new Person();
        person4.setName("jiao");
        person4.setAddress("save4");
        person4.setAge(22);
        Person p = personRepository.save(person4);
        System.out.println(p);
        if (p.getName().equals("jiao")) {
            throw new RuntimeException("save4  jiao已存在，但数据不会回滚");
        }
    }
}
