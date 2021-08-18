package cn.itcast.web.servlet;

import cn.itcast.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @auther baixz
 * @create 2021-08-18 16:15
 */
@WebServlet("/seccuesServlet")
public class SeccuesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User)req.getAttribute("user");

        if (user != null) {
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write("登录成功！" + user.getUsername() + "欢迎您");
        }
    }
}
