#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>

#define FSIZ 60000

static int
cmpstringp(const void *p1, const void *p2)
{
  return strcmp(* (char * const *) p1, * (char * const *) p2);
}

int 
main(int argc, char **argv)
{
  char **first_names = malloc(6000*sizeof(char*));
  char *buf = malloc(FSIZ);
  FILE *f = fopen("names.txt", "r");
  if (f == NULL) {
    fprintf(stderr, "cannot open names.txt\n");
    exit(1);
  }
  size_t siz = fread(buf, 1, FSIZ, f);
  assert(siz<FSIZ);
  fclose(f);

  char **name = first_names;
  char *p = buf;
  int namen = 0;
  for(;;) {
    if (*p == '\"') {
      *name++ = ++p; namen++;
      while(*p != '\"') ++p;
      *p++ = '\0';
      if (*p == '\0') break;
      else ++p;
    }
  }
  qsort(first_names, namen, sizeof(char*), cmpstringp); 
  
  unsigned i = 1;
  unsigned long long total = 0;
  for(name=first_names; *name; ++name, ++i) {
    unsigned ww = 0;
    char *q;
    for(q=*name; *q; ++q)
      ww += *q-'A'+1;
    total += i*ww;
    //printf("%s: %u, %u, %llu\n", *name, i, ww, total);
  }
  printf("total: %llu\n", total);
  
  return 0;
}

