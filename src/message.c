#include "include/message.h"

smtp_message *message_create()
{
    smtp_message *message = malloc(sizeof(smtp_message));
    message->sender = calloc(255, sizeof(char));
    message->recipient = calloc(255, sizeof(char));
    message->body = calloc(1024 * 1024 * 20, sizeof(char));
    message->file = NULL;
    return message;
}

void message_free(smtp_message *message)
{
    free(message->sender);
    free(message->recipient);
    free(message->body);
    free(message);
}