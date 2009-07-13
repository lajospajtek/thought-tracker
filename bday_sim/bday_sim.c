#include <stdio.h>
#include <stdlib.h>

#include <strings.h>
#include <unistd.h>
#include <time.h>
#include <errno.h>
#include <signal.h>

#define DFLT_N 100000
#define DFLT_K 1000
#define DFLT_MAX_ITER 1000000


static unsigned int max_iter, iter, equals, N, K;

static unsigned int *a;

static double
uniform_distrib(double a, double b) {

  return a + (b - a) * ((double)random() / (double)RAND_MAX);
}

static void
onUSR1(int dummy) {
  printf("%f\n", (double)equals / iter);
}

static void
parse_cmd_line(int argc, char *argv[]) {

  int c;
  char *endp;
  extern char *optarg;

  while ((c = getopt(argc, argv, "hN:K:i:")) != EOF)

    switch (c) {
    case '?':
      /* error */
      break;

    case 'h':
      /* help message */
      break;
    case 'N':

      errno = 0;
      if (optarg == NULL) {
        /* error */
      }
      N = (unsigned int)strtoul(optarg, &endp, 0);

      if (errno != 0 || *endp != '\0') {
        /* error */
      }
      break;
    case 'K':
      errno = 0;

      if (optarg == NULL) {
        /* error */
      }

      K = (unsigned int)strtoul(optarg, &endp, 0);
      if (errno != 0 || *endp != '\0') {
        /* error */
      }
      break;
    case 'i':
      errno = 0;

      if (optarg == NULL) {
        /* error */
      }

      max_iter = (unsigned int)strtoul(optarg, &endp, 0);
      if (errno != 0 || *endp != '\0') {
        /* error */
      }
      break;
    }
  if (N == 0)

    N = DFLT_N;
  if (K == 0)

    K = DFLT_K;
  if (max_iter == 0)

    max_iter = DFLT_MAX_ITER;
}

int
main(int argc, char *argv[]) {

  unsigned int i, picked;
  struct sigaction act;

  parse_cmd_line(argc, argv);

  srandom(time(NULL));

  if ((a = malloc(N * sizeof(unsigned int))) == NULL) {

    /* error */
  }

  act.sa_handler = onUSR1;
  act.sa_flags = 0;

  sigemptyset(&act.sa_mask);
  sigaction(SIGUSR1, &act, NULL);

  for (iter = 0; iter < max_iter; ++iter) {

    bzero(a, N * sizeof(unsigned int));
    for (i = 0; i < K; ++i) {

      picked = (unsigned int)uniform_distrib(0.0, (double)N);
      if (a[picked] == 1) {
        ++equals;
        break;
      }
      a[picked] = 1;
    }
  }
    
  onUSR1(SIGUSR1);

  return 0;
}

