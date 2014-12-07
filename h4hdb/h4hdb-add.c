/*
 * mdb-add.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#include "mylist.h"
#include "h4hdb.h"

static void die(const char *message) {
    perror(message);
    exit(1); 
}

static void sanitize(char *s) {
    while (*s) {
        if (!isprint(*s)) 
            *s = ' ';
        s++;
    }
}

int main(int argc, char **argv)
{
    /*
     * open the database file specified in the command line
     */

    if (argc != 2) {
        fprintf(stderr, "%s\n", "usage: mdb-add <database_file>");
        exit(1);
    }

    char *filename = argv[1];
    // open for append & read, in binary mode
    FILE *fp = fopen(filename, "a+b");
    if (fp == NULL) {
        die(filename);
    }

    /*
     * load the database file into a linked list
     */

    struct List list;
    initList(&list);

    // we need to call fseek() to read from the beginning
    fseek(fp, 0, SEEK_SET);

    int loaded = loadmdb(fp, &list);
    if (loaded < 0)
        die("loadmdb");

    // count the number of entries and keep a pointer to the last node
    struct Node *lastNode = list.head;
    int recNo = 0;
    while (lastNode) {
        recNo++;
        if (lastNode->next) {
            lastNode = lastNode->next;
        } else {
            break;
        }
    }

    /*
     * read name
     */

    struct MdbRec r;
    char line[1000];

    printf("name please (will truncate to %d chars): ", (int)sizeof(r.name)-1);
    if (fgets(line, sizeof(line), stdin) == NULL) {
        fprintf(stderr, "%s\n", "could not read name");
        exit(1);
    }
    // must null-terminate the string manually after strncpy().
    strncpy(r.name, line, sizeof(r.name) - 1);
    r.name[sizeof(r.name) - 1] = '\0';
    // if newline is there, remove it.
    size_t last = strlen(r.name) - 1;
    if (r.name[last] == '\n')
        r.name[last] = '\0';

    /*
     * read msg
     */

    printf("phone number please (will truncate to %d chars): ", (int)sizeof(r.pnum)-1);
    if (fgets(line, sizeof(line), stdin) == NULL) {
        fprintf(stderr, "%s\n", "could not read longitude");
        exit(1);
    }
    // must null-terminate the string manually after strncpy().
    strncpy(r.pnum, line, sizeof(r.pnum) - 1);
    r.pnum[sizeof(r.pnum) - 1] = '\0';
    // if newline is there, remove it.
    last = strlen(r.pnum) - 1;
    if (r.pnum[last] == '\n')
        r.pnum[last] = '\0';

    printf("message please (will truncate to %d chars): ", (int)sizeof(r.msg)-1);
    if (fgets(line, sizeof(line), stdin) == NULL) {
        fprintf(stderr, "%s\n", "could not read message");
        exit(1);
    }
    strncpy(r.msg, line, sizeof(r.msg) - 1);
    if (r.msg[last] == '\n')
        r.msg[last] = '\0';

    /*
     * add the record to the in-memory database
     */

    struct MdbRec *rec = (struct MdbRec *)malloc(sizeof(r));
    if (!rec)
        die("malloc failed");
    memcpy(rec, &r, sizeof(r));
    
    struct Node *newNode = addAfter(&list, lastNode, rec);
    if (newNode == NULL) 
        die("addAfter() failed");
    recNo++;

    /*
     * write the name and msg to the database file
     */

    // remove non-printable chars from the strings
    sanitize(r.name);;
    sanitize(r.pnum);
    sanitize(r.msg);

    if (fwrite(&r, sizeof(r), 1, fp) < 1) {
        perror("can't write record");
        exit(1);
    }
    if (fflush(fp) != 0) {
        perror("can't write to file");
        exit(1);
    }

    /*
     * print confirmation
     */

    printf("%4d: name: %s phone number: %s message: %s\n", recNo, rec->name, rec->pnum, rec->msg);
    fflush(stdout);

    /*
     * clean up and exit
     */

    freemdb(&list);
    fclose(fp);
    return 0;
}
