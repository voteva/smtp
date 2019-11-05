#ifndef MESSAGE_H
#define MESSAGE_H

#include <stdio.h>

typedef struct message smtp_message;

struct message
{
    char *sender;
    char *recipient;
    char *body;
    FILE *file;
};

smtp_message *message_create();

void message_free(smtp_message *message);

#endif
