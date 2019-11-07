#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>
#include <time.h>

#include "logger.h"

static struct
{
    FILE *file;
} LOGGER;

static const char *level_names[] = {"TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL"};
static const char *datetime_pattern = "%Y-%m-%d %H:%M:%S";

void log_set_file(FILE *file)
{
    LOGGER.file = file;
}

void log_log(int level, const char *file, int line, const char *fmt, ...)
{
    time_t t = time(NULL);
    struct tm *lt = localtime(&t);

    va_list args;
    char buf[32];
    buf[strftime(buf, sizeof(buf), datetime_pattern, lt)] = '\0';
    fprintf(LOGGER.file, "%s %-5s %s:%d: ", buf, level_names[level], file, line);
    va_start(args, fmt);
    vfprintf(LOGGER.file, fmt, args);
    va_end(args);
    fprintf(LOGGER.file, "\n");
    fflush(LOGGER.file);
}