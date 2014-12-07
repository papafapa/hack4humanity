CC = gcc
CXX = g++

INCLUDES = -I../../cs3157/lab3/solutions/part1
CFLAGS = -g -Wall $(INCLUDES)
CXXFLAGS = -g -Wall $(INCLUDES)

LDFLAGS = -g -L../../cs3157/lab3/solutions/part1
LDLIBS = -lmylist

.PHONY: default
default: h4hdb-add h4hdb-lookup

h4hdb-lookup: h4hdb.o

h4hdb-add: h4hdb.o

h4hdb-add.o: h4hdb.h

h4hdb-lookup.o: h4hdb.h

h4hdb.o: h4hdb.h

./PHONY: clean
clean: 
	rm -f *.o *~ a.out core h4hdb-add h4hdb-lookup

.PHONY: all
all: clean default
