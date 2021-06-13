package com.example.server;

import CaseOffice.*;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class OfficeI implements Office {
    private final Random random = new Random(System.currentTimeMillis());
    private final Map<String, Map<String, Long>> cases = new HashMap<>();

    @Override
    public CaseAck makeCaseRequest(CaseRequest caseRequest, Current current) {
        Map<String, Long> applicantCases = cases.getOrDefault(caseRequest.applicantID, new HashMap<>());
        long delay = (6 + random.nextInt(6)) * 10000;
        long caseResolutionTime = System.currentTimeMillis() + delay;
        String caseID = UUID.randomUUID().toString();

        applicantCases.put(caseID, caseResolutionTime);
        cases.put(caseRequest.applicantID, applicantCases);
        return new CaseAck(caseID, delay);
    }

    @Override
    public synchronized void checkCaseResults(String applicantID, ApplicantPrx applicant, Current current) {
        long currentTime = System.currentTimeMillis();
        CaseResult[] results = cases.get(applicantID).entrySet().stream()
                .filter(entry -> entry.getValue() < currentTime)
                .map(entry -> new CaseResult(entry.getKey(), random.nextBoolean() ? "request accepted" : "request denied"))
                .toArray(CaseResult[]::new);
        try {
            applicant.receiveResults(results);
        } catch (Exception e) {
            return;
        }
        for (CaseResult result : results) {
            cases.remove(result.caseID);
        }
    }
}
