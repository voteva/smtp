#include "configuration.h"

int get_configuration(char *config_path)
{
    config_t cfg;
    //config_setting_t *setting;

    config_init(&cfg);

    if (!config_read_file(&cfg, config_path))
    {
        fprintf(stderr, "%s:%d - %s\n", config_error_file(&cfg), config_error_line(&cfg), config_error_text(&cfg));
        config_destroy(&cfg);
        return (EXIT_FAILURE);
    }

    if (config_lookup_int(&cfg, "port", &CONFIG->port))
    {
        printf("logs_dir: %d\n\n", CONFIG->port);
    } else
    {
        fprintf(stderr, "No 'port' setting in configuration file.\n");
    }

    config_destroy(&cfg);

    return (EXIT_SUCCESS);
}