package cn.itcast.test;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;
import org.junit.Test;

/**
 * @auther baixz
 * @create 2021-08-18 16:01
 */
public class WebTest {
    @Test
    public void test() {
        User loginuser = new User();
        loginuser.setUsername("baixz");
        loginuser.setPassword("123456");

        UserDao userDao = new UserDao();
        User user = userDao.login(loginuser);

        System.out.println(user);
    }

    @Test
    public void test1() {

    }


}
