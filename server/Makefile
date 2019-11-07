CFLAGS=-g -Wall
LDFLAGS=-pthread

all: server

clean:
	rm -rf server *.o

server: listener.o sender.o session.o parser.o list.o server.c

listener.o: session.o sender.o listener.c

sender.o: sender.c

session.o: parser.o session.c

parser.o: list.o parser.c

list.o: list.c
