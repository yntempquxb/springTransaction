package com.jiao.service;

import com.jiao.NoRollbackException;
import com.jiao.Person;
import com.jiao.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class Support {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    Helper helper;


    public void save2(Person person) {
        Person p = personRepository.save(person);
        System.out.println(p);
        if (person.getName().equals("jiao")) {
            throw new IllegalArgumentException("jiao已存在，但数据不会回滚");
        }
    }

    //必须声明式 ： throws NoRollbackException
    public void save3(Person person) throws NoRollbackException {
        Person p = personRepository.save(person);
        System.out.println(p);
        if (person.getName().equals("jiao")) {
            throw new NoRollbackException("jiao已存在，但数据不会回滚");
        }
    }

    //必须声明式 ： throws NoRollbackException
    public void save4(Person person) throws NoRollbackException {
        Person p = personRepository.save(person);
        System.out.println(p);

        try {
            helper.save4();
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoRollbackException("jiao已存在，但数据不会回滚");
        }

    }

    @Transactional(noRollbackFor = IllegalArgumentException.class)
    //指定不会滚的异常
    public void save5(Person person) {
        try {
            Person p = personRepository.save(person);
            if (person.getName().equals("jiao")) {
                System.out.println(person.getName());
            }
            System.out.println(p);
        } catch (Exception e) {
            throw new IllegalArgumentException("jiao已存在，但数据不会回滚");
        }
    }
}
