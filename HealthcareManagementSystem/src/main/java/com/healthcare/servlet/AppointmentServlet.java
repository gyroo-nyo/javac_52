package com.healthcare.servlet;

import com.healthcare.dao.AppointmentDAO;
import com.healthcare.model.Appointment;

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
@WebServlet(name = "AppointmentServlet", urlPatterns = {"/appointments"})
public class AppointmentServlet extends HttpServlet {
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Appointment> list = appointmentDAO.findAll();
        PrintWriter out = resp.getWriter();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            Appointment a = list.get(i);
            json.append(String.format("{\"id\":%d,\"patient\":\"%s\",\"doctor\":\"%s\",\"date\":\"%s\",\"status\":\"%s\"}",
                    a.getId(), a.getPatientName(), a.getDoctorName(), a.getAppointmentDate(), a.getStatus()));
            if (i < list.size() - 1) json.append(",");
        }
        json.append("]");
        out.print(json.toString());
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int patientId = Integer.parseInt(req.getParameter("patientId"));
        String patientName = req.getParameter("patientName");
        int doctorId = Integer.parseInt(req.getParameter("doctorId"));
        String doctorName = req.getParameter("doctorName");
        String date = req.getParameter("date");
        String time = req.getParameter("time");
        String notes = req.getParameter("notes");

        try {
            boolean ok = appointmentDAO.bookAppointmentTransaction(patientId, patientName, doctorId, doctorName, date, time, notes);
            resp.setStatus(ok ? HttpServletResponse.SC_CREATED : HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\":" + ok + "}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
