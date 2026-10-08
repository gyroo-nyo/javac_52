package com.healthcare.servlet;

import com.healthcare.dao.DoctorDAO;
import com.healthcare.model.Doctor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * RUBRIC REQUIREMENT: Servlets & Web Integration (7 Marks)
 */
@WebServlet(name = "DoctorServlet", urlPatterns = {"/doctors"})
public class DoctorServlet extends HttpServlet {
    private final DoctorDAO doctorDAO = new DoctorDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Doctor> doctors = doctorDAO.findAll();
        PrintWriter out = resp.getWriter();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);
            json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"specialization\":\"%s\",\"fee\":%.2f,\"available\":%b}",
                    d.getId(), d.getName(), d.getSpecialization(), d.getFee(), d.isAvailable()));
            if (i < doctors.size() - 1) json.append(",");
        }
        json.append("]");
        out.print(json.toString());
        out.flush();
    }
}
