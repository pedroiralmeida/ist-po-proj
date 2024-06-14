all:
	(cd src/prr-core; make $(MFLAGS) all)
	(cd src/prr-app; make $(MFLAGS) all)

clean:
	(cd src/prr-core; make $(MFLAGS) clean)
	(cd src/prr-app; make $(MFLAGS) clean)

install:
	(cd src/prr-core; make $(MFLAGS) install)
	(cd src/prr-app; make $(MFLAGS) install)
