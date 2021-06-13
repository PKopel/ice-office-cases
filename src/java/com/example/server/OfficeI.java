package com.example.server;

import CaseOffice.*;
import com.zeroc.Ice.Current;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OfficeI implements Office {
    private final Random random = new Random(System.currentTimeMillis());
    private final Map<String, Map<String, CaseRecord>> cases = new HashMap<>();

    @Override
    public CaseAck makeCaseRequest(CaseRequest caseRequest, Current current) {
        System.out.println("new case from applicant " + caseRequest.applicantID);
        Map<String, CaseRecord> applicantCases = cases.getOrDefault(caseRequest.applicantID, new HashMap<>());
        CaseRecord caseRecord = new CaseRecord(caseRequest);
        applicantCases.put(caseRecord.getCaseID(), caseRecord);
        cases.put(caseRequest.applicantID, applicantCases);
        return new CaseAck(caseRecord.getCaseID(), caseRecord.getCaseResolutionTime());
    }

    @Override
    public synchronized long checkCaseResults(String applicantID, ApplicantPrx applicant, Current current) {
        System.out.println("checking results for applicant " + applicantID);
        long currentTime = System.currentTimeMillis();
        Map<String, CaseRecord> applicantsCases = cases.getOrDefault(applicantID, new HashMap<>());
        CaseResult[] results = applicantsCases.entrySet().stream()
                .filter(entry -> entry.getValue().getCaseResolutionTime() < currentTime)
                .map(entry -> new CaseResult(entry.getKey(), entry.getValue().resolveCase() ? "request accepted" : "request denied"))
                .toArray(CaseResult[]::new);
        if (results.length == 0 && !applicantsCases.isEmpty()) {
            return applicantsCases.values().stream().sorted().findFirst().get().getCaseResolutionTime();
        } else if (results.length != 0) {
            try {
                System.out.println(Arrays.toString(results));
                if (applicant.receiveResults(results)) {
                    System.out.println("removing old cases of applicant " + applicantID);
                    for (CaseResult result : results) {
                        applicantsCases.remove(result.caseID);
                    }
                    cases.put(applicantID, applicantsCases);
                }
            } catch (Exception e) {
                System.err.println("Error while sending results to client:");
                e.printStackTrace();
            }
        }
        return 0;
    }
}
