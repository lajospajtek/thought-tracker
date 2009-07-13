#include <stdio.h>
#include <math.h>

int main(int argc, char ** argv)
{
    unsigned i = 0L;
    unsigned long long sum = 0L;
    while (++i<1001) {
        unsigned long long p;
        unsigned j;
        for (p=1L,j=0; j<i; ++j) { 
            p *= i;
            p = p % 10000000000LL; // why do i need this?
        }
        sum += p;
    }
    printf("%llu\n", sum%10000000000LL);
    return 0;
}

