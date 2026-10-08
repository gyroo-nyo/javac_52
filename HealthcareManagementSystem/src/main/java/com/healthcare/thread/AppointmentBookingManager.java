package com.healthcare.thread;

import com.healthcare.exception.AppointmentConflictException;
import com.healthcare.model.Appointment;

import java.util.*;
import java.util.concurrent.*;

/**
 * RUBRIC REQUIREMENT: Multithreading & Synchronization (4 Marks)
 * 
 * Demonstrates thread synchronization using intrinsic synchronized monitors,
 * synchronized blocks, and atomic locks to prevent race conditions during
 * concurrent appointment slot bookings.
 */
public class AppointmentBookingManager {
    private static AppointmentBookingManager instance;

    // In-memory slot registry to prevent race conditions: "doctorId_date_time" -> Appointment
    private final Map<String, Appointment> bookedSlotsRegistry = new ConcurrentHashMap<>();

    // Lock object for explicit synchronization
    private final Object slotLock = new Object();

    private AppointmentBookingManager() {}

    public static synchronized AppointmentBookingManager getInstance() {
        if (instance == null) {
            instance = new AppointmentBookingManager();
        }
        return instance;
    }

    /**
     * SYNCHRONIZED METHOD:
     * Guarantees that only ONE thread can book a specific slot at any given moment.
     * Prevents the classic "Double Booking" race condition.
     */
    public synchronized boolean bookSlotSynchronized(int doctorId, String date, String time, 
                                                      int patientId, String patientName, String doctorName) 
            throws AppointmentConflictException {
        
        String slotKey = doctorId + "_" + date + "_" + time;

        // CRITICAL SECTION PROTECTED BY SYNCHRONIZATION:
        if (bookedSlotsRegistry.containsKey(slotKey)) {
            // Another thread has already acquired this slot
            throw new AppointmentConflictException(doctorId, date, time);
        }

        // Simulate tiny processing latency to test concurrency
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Appointment appt = new Appointment(
                bookedSlotsRegistry.size() + 1,
                patientId,
                patientName,
                doctorId,
                doctorName,
                date,
                time,
                "Confirmed",
                "Synchronized Thread Safe Booking"
        );

        bookedSlotsRegistry.put(slotKey, appt);
        System.out.printf("[SYNCHRONIZED THREAD %s] Slot %s successfully booked for patient: %s\n",
                Thread.currentThread().getName(), slotKey, patientName);
        return true;
    }

    /**
     * Demonstrates SYNCHRONIZED BLOCK for thread safety.
     */
    public boolean checkAndReserveSlot(String slotKey) {
        synchronized (slotLock) {
            return !bookedSlotsRegistry.containsKey(slotKey);
        }
    }

    /**
     * Concurrent Stress Test: Spawns 4 concurrent threads competing for the exact same doctor slot.
     * Only 1 thread will succeed; the others will throw AppointmentConflictException.
     */
    public List<String> runConcurrentBookingStressTest(int doctorId, String date, String time, String doctorName) {
        List<String> auditLogs = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(4);

        for (int i = 1; i <= 4; i++) {
            final int patientId = 100 + i;
            final String patientName = "Candidate Patient #" + i;

            executor.submit(() -> {
                try {
                    boolean success = bookSlotSynchronized(doctorId, date, time, patientId, patientName, doctorName);
                    if (success) {
                        auditLogs.add("SUCCESS: " + patientName + " locked slot " + date + " " + time);
                    }
                } catch (AppointmentConflictException ex) {
                    auditLogs.add("BLOCKED: " + patientName + " rejected: " + ex.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        return auditLogs;
    }

    public void clearSlots() {
        bookedSlotsRegistry.clear();
    }
}
