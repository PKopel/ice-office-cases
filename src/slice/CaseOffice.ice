
module CaseOffice {
    enum CaseType {
        ConstructionPermission,
        DrivingLicense,
        PassportRequest,
    };

    struct CaseAck {
        string caseID;
        long expectedReplyTime;
    };

    struct CaseResult {
        string caseID;
        string result;
    };

    dictionary<string, string> AdditionalData;

    struct CaseRequest {
        string applicantID;
        CaseType caseType;
        AdditionalData additionalData;
    };

    sequence<CaseResult> CaseResults;

    interface Applicant {
        bool receiveResults(CaseResults caseResults);
    };

    interface Office {
        CaseAck makeCaseRequest(CaseRequest caseRequest);
        long checkCaseResults(string applicantID, Applicant* applicant);
    };
};