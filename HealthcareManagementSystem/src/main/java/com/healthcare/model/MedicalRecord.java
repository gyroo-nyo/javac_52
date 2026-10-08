package com.healthcare.model;

/**
 * Medical Record Model
 */
public class MedicalRecord {
    private int id;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private String visitDate;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String notes;

    public MedicalRecord(int id, int patientId, String patientName, int doctorId, String doctorName,
                         String visitDate, String symptoms, String diagnosis, String treatment, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.visitDate = visitDate;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public int getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getVisitDate() { return visitDate; }
    public String getSymptoms() { return symptoms; }
    public String getDiagnosis() { return diagnosis; }
    public String getTreatment() { return treatment; }
    public String getNotes() { return notes; }
}
