package com.example.server;

import CaseOffice.CaseRequest;
import CaseOffice.CaseType;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class CaseRecord {
    private final Random random = new Random(System.currentTimeMillis());
    private final String caseID;
    private final CaseType caseType;
    private final Map<String, String> additionalData;
    private final long caseResolutionTime;

    CaseRecord(CaseRequest request) {
        long delay = (6 + random.nextInt(6)) * 10000;
        caseResolutionTime = System.currentTimeMillis() + delay;
        caseID = UUID.randomUUID().toString();
        caseType = request.caseType;
        additionalData = request.additionalData;
    }

    public boolean resolveCase() {
        return switch (caseType) {
            case ConstructionPermission -> additionalData.containsKey("address");
            case DrivingLicense -> Integer.parseInt(additionalData.getOrDefault("age", "0")) > 16;
            case PassportRequest -> random.nextBoolean();
        };
    }

    public String getCaseID() {
        return caseID;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    public long getCaseResolutionTime() {
        return caseResolutionTime;
    }
}
