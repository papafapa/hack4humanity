CC = gcc
CXX = g++

INCLUDES = -I../h4hlist
CFLAGS = -g -Wall $(INCLUDES)
CXXFLAGS = -g -Wall $(INCLUDES)

LDFLAGS = -g -L../h4hlist
LDLIBS = -lh4hlist

./PHONY: default
default: h4hdb-lookup-server

h4hdb-lookup-server.o: h4hdb.h

.PHONY: clean
clean:
	rm -f *.o *~ a.out core h4hdb-lookup-server

.PHONY: all
all: clean default
