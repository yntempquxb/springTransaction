package com.jiao.service.impl;

import com.jiao.NoRollbackException;
import com.jiao.NoRollbackException2;
import com.jiao.Person;
import com.jiao.PersonRepository;
import com.jiao.service.DemoService;
import com.jiao.service.Support;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2018/2/26.
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    Support support;

    // 写 try 入库

    @Transactional(rollbackFor = {IllegalArgumentException.class})
    @Override
    public Person savePersonWithRollBack(Person person) {
        Person p = personRepository.save(person);
        if (true) {
            throw new IllegalArgumentException("jiao 已存在，数据将回滚");
        }
        return p;
    }


    @Transactional(noRollbackFor = {IllegalArgumentException.class})
    @Override
    public Person savePersonWithoutRollBack(Person person) {
        Person p = personRepository.save(person);
        if (true) {
            throw new IllegalArgumentException("jiao已存在，但数据不会回滚");
        }
        return p;
    }

    @Transactional(noRollbackFor = {NoRollbackException2.class})
    @Override
    public Person savePersonWithoutRollBack2(Person person) {

        Person p = personRepository.save(person);

        Person person1 = new Person();
        person1.setName("jiao");
        person1.setAddress("11111111");
        person1.setAge(19);

        //存在事物传播混乱，noRollbackFor = {IllegalArgumentException.class} 将不生效
        try {
            support.save2(person1);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("存在事物传播混乱, 报错；Transaction marked as rollbackOnly ，noRollbackFor = {IllegalArgumentException.class} 将不生效 数据会回滚");
            throw new NoRollbackException2(e);
        }
        return person1;
    }

    @Transactional
    @Override
    public Person savePersonWithoutRollBack3(Person person) {

        Person p = personRepository.save(person);

        Person person1 = new Person();
        person1.setName("jiao");
        person1.setAddress("11111111");
        person1.setAge(19);

        try {
            support.save3(person1);//但是person persion1 都落库了
        } catch (NoRollbackException e) {
            e.printStackTrace();
            System.out.println("存在事物传播混乱，无须noRollbackFor = {NoRollbackException.class} 数据不会回滚, 这才是想要的效果");
            System.out.println("产生的原因是，spring注解事务采用AOP，只针对 RuntimeException.class 的异常才回滚");
        }

        return person1;
    }

    @Transactional
    @Override
    public Person savePersonWithoutRollBack4(Person person) {

        Person p = personRepository.save(person);

        Person person1 = new Person();
        person1.setName("jiao");
        person1.setAddress("11111111");
        person1.setAge(19);

        //存在事物传播混乱，有NoRollbackException也没用
        try {
            support.save4(person1);//都回滚---故save4
        } catch (NoRollbackException e) {
            e.printStackTrace();
            System.out.println("存在事物传播混乱，有NoRollbackException也没用");
        }

        return person1;
    }

    /**
     * 查看exception类图
     * https://blog.csdn.net/Cary_1029/article/details/84945166
     *
     * 目标：try catch 只要捕获了 就不能影响流程
     *
     * 目的：针对一些  不应该影响主流程的部分，只要打印记录 或 保存记录 即可
     *
     * 综述测试结论  ：
     *
     *    try catch 不回滚方案:
     *    一  NoRollbackException1 extend Exception
     *        1 需要不回滚的地方 手动 throw new NoRollbackException1 非运行时 异常
     *        2 问题：但是一旦有一个RuntimeException 则 回滚
     *
     *    二  NoRollbackException2 extend RuntimeException
     *        1 @Transactional(noRollbackFor = {NoRollbackException2.class})
     *        2 需要不回滚的地方 throw new NoRollbackException2 extend RuntimeException
     *        3 严格把控注解@Transactional 不可以到处加， 只有api对应的impl可以加 其他一律不加
     *        4 问题：combined dubbo 组合了多个impl 一律不可以加@Transactional
     *               同一个dubbo中 立案 调用 自动核损  两个都加了@Transactional 出现了 传播混乱
     *        5 各种不确定的  传播混乱情况
     *
     * 选择方案：1 ？  2 ？ 讨论！！
     *
     * 本人推荐：
     *    方案1 ：
     *      好处：1 产生RuntimeException 原因
     *                 开发写的代码不合理，必须改代码---虽说不应该影响流程，但是不能产生  类型转换异常 空指针异常 等
     *                 数据传值不合理，必须改正
     *                 三方系统给你抛RuntimeException --- 这种 永远都回滚
     *           2  只有三方系统抛Exception 这种 我们才不会 阻断流程
     *           3  方案2 管控不住
     *           4  步阻碍流程 可以考虑异步
     *
     *
     */

}