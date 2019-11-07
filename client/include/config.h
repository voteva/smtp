#ifndef CONFIG_H
#define CONFIG_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libconfig.h>

#define DEFAULT_CONFIG_PATH "./client.cfg"
#define DEFAULT_LOGS_DIR "./out.log"
#define DEFAULT_PORT 587

typedef struct
{
    int port;
    char logs_dir[];
} config;

config *CONFIG;

int get_config(char *config_path);

#endif