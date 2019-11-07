#include "client.h"
#include "configuration.h"
#include "logger.h"

#include <libconfig.h>

int main(int argc, char **argv)
{
    FILE *file = fopen("./out.log", "w");

    log_set_file(file);
    log_info("Hello");

    CONFIG = (configuration *) malloc(sizeof(configuration));
    if (get_configuration(argv[1]) == EXIT_FAILURE)
    {
        return 0;
    }
}