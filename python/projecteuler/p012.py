# p 012
from math import sqrt

i = 1
tn = 0
divr = 0
while divr<500:
    tn += i
    i += 1
    divr=2
    for j in range(2, int(sqrt(tn))+2):
        if tn % j == 0:
            divr +=2

print i, tn, divr

