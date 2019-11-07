#ifndef CONFIGURATION_H
#define CONFIGURATION_H

#include <stdio.h>
#include <stdlib.h>
#include <libconfig.h>

typedef struct
{
    int port;
    //const char[] logs_dir;
} configuration;

configuration *CONFIG;

int get_configuration(char *config_path);

#endif