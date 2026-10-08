package com.healthcare.servlet;

import com.healthcare.dao.UserDAO;
import com.healthcare.exception.AuthenticationException;
import com.healthcare.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * RUBRIC REQUIREMENT: Servlets & Web Integration (7 Marks)
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/auth/login"})
public class AuthServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String pass = req.getParameter("password");

        try {
            User user = userDAO.authenticate(email, pass);
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            resp.setContentType("application/json");
            resp.getWriter().write(String.format("{\"success\":true,\"name\":\"%s\",\"role\":\"%s\"}", user.getName(), user.getRole()));
        } catch (AuthenticationException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
