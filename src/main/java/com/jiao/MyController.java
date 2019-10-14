package com.jiao;

import com.jiao.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/2/26.
 */
@RestController
public class MyController {
    @Autowired
    private DemoService demoService;

    @RequestMapping("/norollback")
    public Person noRollback(@RequestBody Person person) {
        return demoService.savePersonWithoutRollBack(person);
    }
    @RequestMapping("/norollback2")
    public Person noRollback2(@RequestBody Person person) {
        return demoService.savePersonWithoutRollBack2(person);
    }
    @RequestMapping("/norollback3")
    public Person noRollback3(@RequestBody Person person) {
        return demoService.savePersonWithoutRollBack3(person);
    }
    @RequestMapping("/norollback4")
    public Person noRollback4(@RequestBody Person person) {
        return demoService.savePersonWithoutRollBack4(person);
    }

    @RequestMapping("/rollback")
    public Person rollback(@RequestBody Person person) {
        return demoService.savePersonWithRollBack(person);
    }
}