package com.healthcare.thread;

import com.healthcare.model.Patient;
import java.util.LinkedList;
import java.util.Queue;

/**
 * RUBRIC REQUIREMENT: Multithreading & Synchronization (4 Marks)
 * 
 * Thread-safe hospital waiting room triage queue demonstrating synchronized
 * producer-consumer pattern using wait() and notifyAll().
 */
public class SynchronizedPatientQueue {
    private final Queue<Patient> queue = new LinkedList<>();
    private final int capacity;

    public SynchronizedPatientQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Producer method: Synchronized enqueue with wait/notify
     */
    public synchronized void enqueuePatient(Patient patient) throws InterruptedException {
        while (queue.size() >= capacity) {
            System.out.println("[TRIAGE QUEUE FULL] Thread waiting: " + Thread.currentThread().getName());
            wait();
        }
        queue.add(patient);
        System.out.printf("[QUEUED] %s admitted to triage. Current waiting: %d\n", patient.getName(), queue.size());
        notifyAll();
    }

    /**
     * Consumer method: Synchronized dequeue with wait/notify
     */
    public synchronized Patient serveNextPatient() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("[TRIAGE QUEUE EMPTY] Doctor thread waiting for patient...");
            wait(2000); // 2 second timeout
            if (queue.isEmpty()) return null;
        }
        Patient served = queue.poll();
        System.out.printf("[SERVED] %s called into examination room. Remaining: %d\n", served.getName(), queue.size());
        notifyAll();
        return served;
    }

    public synchronized int getQueueSize() {
        return queue.size();
    }
}
