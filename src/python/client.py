import Ice
import sys
import threading
import time
from overrides import overrides
from typing import List

import CaseOffice

PROMPT_MESSAGE = """
1: construction permission
2: driving license
3: passport
4: quit
Your request: """


class ApplicantI(CaseOffice.Applicant):

    def __init__(self, output_lock: threading.Lock):
        self.output_lock = output_lock

    @overrides
    def receiveResults(self, caseResults: List[CaseOffice.CaseResult], current=None):
        for result in caseResults:
            with self.output_lock:
                print(f"Case: {result.caseID}, result: {result.result}")
        return True


class Client:
    output_lock = threading.Lock()
    cases_lock = threading.Lock()
    cases = []

    def __init__(self, uid: int, communicator: Ice.CommunicatorI):
        self.applicant_id = uid
        self.office_proxy = CaseOffice.OfficePrx.checkedCast(
            communicator.propertyToProxy("Office.Proxy").ice_twoway().ice_timeout(-1).ice_secure(False)
        )
        self.adapter = communicator.createObjectAdapter("Applicant")
        self.adapter.add(ApplicantI(self.output_lock), Ice.stringToIdentity("applicantI"))
        self.adapter.activate()
        self.applicant_proxy = CaseOffice.ApplicantPrx.uncheckedCast(
            self.adapter.createProxy(Ice.stringToIdentity("applicantI"))
        )

        name = input("name: ")
        age = input("age: ")
        self.additional_data = {"name": name, "age": age}
        threading.Thread(
            target=self.checking_daemon,
            daemon=True
        ).start()

        while self.parse_input():
            pass

    def checking_daemon(self):
        print("checking results")
        time_to_be_ready = self.office_proxy.checkCaseResults(self.applicant_id, self.applicant_proxy)
        if time_to_be_ready != 0:
            with self.cases_lock:
                self.cases.append(time_to_be_ready)
        while True:
            current_time = time.time() * 1000
            with self.cases_lock:
                if len(self.cases) > 0 and self.cases[0] < current_time:
                    print("checking results")
                    self.office_proxy.checkCaseResults(self.applicant_id, self.applicant_proxy)
                    self.cases.pop(0)
            time.sleep(30)

    def parse_input(self):
        request = input(PROMPT_MESSAGE)
        request = int(request.rstrip())

        if request != 4:
            data = self.create_request_data(request)
        else:
            return False

        request_ack = self.office_proxy.makeCaseRequest(data)
        with self.cases_lock:
            self.cases.append(request_ack.expectedReplyTime)
            self.cases.sort()
        current_time = time.time() * 1000
        waiting_time = request_ack.expectedReplyTime - current_time
        with self.output_lock:
            print(f"waiting for result of case {request_ack.caseID}, expected waiting time: {waiting_time} ms")
        return True

    def create_request_data(self, request_type_no: int) -> CaseOffice.CaseRequest:
        if request_type_no == 1:
            request = CaseOffice.CaseType.ConstructionPermission
        elif request_type_no == 2:
            request = CaseOffice.CaseType.DrivingLicense
        elif request_type_no == 3:
            request = CaseOffice.CaseType.PassportRequest

        additional_data = input("additional data ('<key>: <value>,'): ")
        additional_data = additional_data.split(',')
        additional_data_dict = {}
        for data in additional_data:
            key, val = data.split(':')
            additional_data_dict[key] = val

        return CaseOffice.CaseRequest(
            self.applicant_id,
            request,
            {**self.additional_data, **additional_data_dict}
        )


if __name__ == '__main__':
    with Ice.initialize(sys.argv, sys.argv[1]) as comm:
        try:
            cli = Client(sys.argv[2], comm)
        except Exception:
            print("oops, something went wrong. Please restart the client program")
            sys.exit(1)
