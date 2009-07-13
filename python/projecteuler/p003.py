# projecteuler problem 003
# The prime factors of 13195 are 5, 7, 13 and 29.
# What is the largest prime factor of the number 600851475143 ?

from math import sqrt
import math

#n = 13195
n = 600851475143

h = round(sqrt(n)) 
i = 3
a = []
while (i<=h):
    if (n%i == 0): 
        for j in a:
            if (i%j==0): break
        else:
            a.append(i)
    i+=2

print a
