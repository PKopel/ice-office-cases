GEN=src
GEN_JAVA=$(GEN)/java
GEN_PYTHON=$(GEN)/python
$(GEN):
	mkdir $(GEN)

$(GEN_JAVA): | $(GEN)
	mkdir $(GEN_JAVA)

$(GEN_PYTHON): | $(GEN)
	mkdir $(GEN_PYTHON)

slice_java: | $(GEN_JAVA)
	slice2java --output-dir $(GEN_JAVA) slice/CaseOffice.ice

slice_python: | $(GEN_PYTHON)
	slice2py --output-dir $(GEN_PYTHON) slice/CaseOffice.ice
	pip3 install -r requirements.txt