CC = gcc
CXX = g++

INCLUDES =
CFLAGS = -g -Wall $(INCLUDES)
CXXFLAGS = -g -Wall $(INCLUDES)

LDFLAGS = -g
LDLIBS = 

h4hlist-test: libh4hlist.a

libh4hlist.a: h4hlist.o
	ar rc libh4hlist.a h4hlist.o
	ranlib libh4hlist.a

h4hlist-test.o: h4hlist.h
h4hlist.o: h4hlist.h

.PHONY: clean
clean:
	rm -f *.o *~ a.out core h4hlist.a h4hlist-test

.PHONY: all
all: clean libh4hlist.a h4hlist-test
