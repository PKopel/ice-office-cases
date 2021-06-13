GEN=src
GEN_JAVA=$(GEN)/java
GEN_PYTHON=$(GEN)/python
BIN=bin
CLASS_PATH=./lib/icegridgui.jar:./lib/bzip2-1.0.jar:./lib/commons-compress-1.20.jar

$(GEN):
	mkdir $(GEN)

$(GEN_JAVA): | $(GEN)
	mkdir $(GEN_JAVA)

$(GEN_PYTHON): | $(GEN)
	mkdir $(GEN_PYTHON)

$(BIN):
	mkdir $(BIN)

slice_java: | $(GEN_JAVA)
	slice2java --output-dir $(GEN_JAVA) src/slice/CaseOffice.ice

slice_python: | $(GEN_PYTHON)
	slice2py --output-dir $(GEN_PYTHON) src/slice/CaseOffice.ice

build_java: slice_java | $(BIN)
	find -name "*.java" > sources.txt
	rm -rf $(BIN)/*
	javac @sources.txt -d $(BIN) -cp $(CLASS_PATH)
	rm sources.txt

setup_python:
	pip3 install -r requirements.txt

SERVER_ICE_CONFIG=config.server
CLIENT_ICE_CONFIG=config.client
CLIENT_ID = 1234

run_server:
	java -Dfile.encoding=UTF-8 -classpath $(BIN):$(CLASS_PATH) com.example.server.Server --Ice.Config=$(SERVER_ICE_CONFIG)

run_client:
	python3 src/python/client.py $(CLIENT_ICE_CONFIG) $(CLIENT_ID)