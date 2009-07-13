# p 010: sum of primes below 2000000
from math import sqrt
primes=[2,3]
sum = 5
for i in range(5,2000000,2):
    l = int(sqrt(i))
    for d in primes[1:l+1]:
        if i % d == 0: break
    else: 
        #print i
        primes.append(i)
        sum += i
print sum
