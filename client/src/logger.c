#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>
#include <time.h>

#include "logger.h"

static struct
{
    FILE *file;
} LOG;

static const char *level_names[] = {"TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL"};

void log_set_file(FILE *file)
{
    LOG.file = file;
}

void log_log(int level, const char *file, int line, const char *fmt, ...)
{
    time_t t = time(NULL);
    struct tm *lt = localtime(&t);

    va_list args;
    char buf[32];
    buf[strftime(buf, sizeof(buf), "%Y-%m-%d %H:%M:%S", lt)] = '\0';
    fprintf(LOG.file, "%s %-5s %s:%d: ", buf, level_names[level], file, line);
    va_start(args, fmt);
    vfprintf(LOG.file, fmt, args);
    va_end(args);
    fprintf(LOG.file, "\n");
    fflush(LOG.file);
}