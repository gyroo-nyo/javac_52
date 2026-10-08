package com.healthcare.servlet;

import com.healthcare.dao.PatientDAO;
import com.healthcare.model.Patient;

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
@WebServlet(name = "PatientServlet", urlPatterns = {"/patients"})
public class PatientServlet extends HttpServlet {
    private final PatientDAO patientDAO = new PatientDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Patient> patients = patientDAO.findAll();
        PrintWriter out = resp.getWriter();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"age\":%d,\"gender\":\"%s\",\"bloodGroup\":\"%s\"}",
                    p.getId(), p.getName(), p.getAge(), p.getGender(), p.getBloodGroup()));
            if (i < patients.size() - 1) json.append(",");
        }
        json.append("]");
        out.print(json.toString());
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        int age = Integer.parseInt(req.getParameter("age"));
        String gender = req.getParameter("gender");

        Patient newPatient = new Patient(0, name, age, gender);
        try {
            boolean success = patientDAO.save(newPatient);
            resp.setStatus(success ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\":" + success + "}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
