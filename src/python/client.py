import Ice
import sys
import threading
from overrides import overrides

import CaseOffice


class ApplicantI(CaseOffice.Applicant):

    def __init__(self, output_lock):
        self.output_lock = output_lock

    @overrides
    def receiveResults(self, caseResults, current=None):
        for result in caseResults:
            with self.output_lock:
                print(f"Case: {result.caseID}, result: {result.result} \n")


class CommandLineInterface:
    output_lock = threading.Lock()

    def __init__(self, uid, communicator: Ice.CommunicatorI):
        self.applicant_id = uid
        self.office_proxy = CaseOffice.OfficePrx.checkedCast(
            communicator.propertyToProxy('Office.Proxy').ice_twoway().ice_timeout(-1).ice_secure(False)
        )
        self.adapter = communicator.createObjectAdapter("Applicant")
        self.adapter.add(ApplicantI(self.output_lock), Ice.stringToIdentity("applicantI"))
        self.adapter.activate()
        self.applicant_proxy = CaseOffice.ApplicantPrx.uncheckedCast(
            self.adapter.createProxy(Ice.stringToIdentity("applicantI"))
        )

        name = input("name:\n")
        age = input("age:\n")
        self.additional_data = {"name": name, "age": age}
        self.office_proxy.checkCaseResults(self.applicant_id, self.applicant_proxy)
        self.checkResults = lambda: self.office_proxy.checkCaseResults(self.applicant_id, self.applicant_proxy)

    def parse_request(self):
        request = input("""
1: construction permission
2: driving license
3: passport
4: quit
Your request:
""")
        request = int(request.rstrip())
        if request == 4:
            return False
        elif request == 1:
            request = CaseOffice.CaseType.ConstructionPermission
        elif request == 2:
            request = CaseOffice.CaseType.DrivingLicense
        elif request == 3:
            request = CaseOffice.CaseType.PassportRequest

        data = CaseOffice.CaseRequest(
            self.applicant_id,
            request,
            self.additional_data
        )

        with self.output_lock:
            print(f"sending request {data}\n")
        request_ack = self.office_proxy.makeCaseRequest(data)
        with self.output_lock:
            print(f"waiting for result of case {request_ack.caseID}, expected waiting time: {request_ack.expectedReplyTime} ms")
        threading.Timer(request_ack.expectedReplyTime + 10, self.checkResults)
        return True


if __name__ == '__main__':
    with Ice.initialize(sys.argv, "config.client") as communicator:
        try:
            cli = CommandLineInterface(sys.argv[1], communicator)

            while cli.parse_request():
                pass

        except Exception:
            raise RuntimeError("Connection to server failed. Restart needed.")
