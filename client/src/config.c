#include "config.h"

int get_config(char *config_path)
{
    config_t cfg;
    config_init(&cfg);

    if (!config_read_file(&cfg, config_path))
    {
        fprintf(stderr, "Error while reading configuration file %s: %s\n", config_path, config_error_text(&cfg));
        config_destroy(&cfg);
        return (EXIT_FAILURE);
    }

    if (!config_lookup_int(&cfg, "port", &CONFIG->port))
    {
        CONFIG->port = DEFAULT_PORT;
    }

    const char *logs_dir_ptr;
    if (!config_lookup_string(&cfg, "logs_dir", &logs_dir_ptr))
    {
        strcpy(CONFIG->logs_dir, DEFAULT_LOGS_DIR);
    } else
    {
        strcpy(CONFIG->logs_dir, logs_dir_ptr);
    }

    config_destroy(&cfg);

    return (EXIT_SUCCESS);
}