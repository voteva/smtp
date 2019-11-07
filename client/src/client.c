#include "client.h"
#include "config.h"
#include "logger.h"

char *get_config_path(int argc, char *argv[])
{
    if (argc >= 2) return argv[1];
    return DEFAULT_CONFIG_PATH;
}

int main(int argc, char **argv)
{
    char *config_path = get_config_path(argc, argv);

    CONFIG = (config *) malloc(sizeof(config));

    if (get_config(config_path) == EXIT_FAILURE)
    {
        return (EXIT_FAILURE);
    }

    FILE *file = fopen(CONFIG->logs_dir, "w");

    log_set_file(file);
    log_info("Hello");
}