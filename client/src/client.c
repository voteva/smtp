#include "client.h"
#include "logger.h"

int main(int argc, char **argv)
{
    FILE *file = fopen("./out.log", "w");

    log_set_file(file);
    log_info("Hello");
}