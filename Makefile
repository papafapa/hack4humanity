#
#Makefile for lab 7
#

CC  = gcc
CXX = g++

INCLUDES = -I../Hack4Humanity/lab3/part1
CFLAGS   = -g -Wall $(INCLUDES)
CXXFLAGS = -g -Wall $(INLCUDES)

LDFLAGS  = -g -L../lab3/part1
LDLIBS   = 

.PHONY: default
default: http-server 


.PHONY: clean
clean:
	rm -f *.o *~ a.out core http-server

.PHONY: all
all: clean default

