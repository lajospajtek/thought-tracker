# p 050:
# The prime 41, can be written as the sum of six consecutive primes:
# 41 = 2 + 3 + 5 + 7 + 11 + 13
# This is the longest sum of consecutive primes that adds to a prime below 
# one-hundred. The longest sum of consecutive primes below one-thousand that 
# adds to a prime, contains 21 terms, and is equal to 953. Which prime, below 
# one-million, can be written as the sum of the most consecutive primes?

#answer: 997651 sum of 543 terms starting with 7

from math import sqrt

primes=[2,3]
for i in range(5,999999,2):
  l = int(sqrt(i))
  for d in primes[1:l+1]:
    if i % d == 0: break
  else: primes.append(i)

max_t = 0
for i in range(0,len(primes)):
  terms =[]
  sum = 0
  if primes[i] > primes[-1]/2: break
  for p in primes[i:]:
    sum += p
    terms.append(p)
    if sum > primes[-1]: break
    if sum in primes and len(terms) > max_t:
      max_t = len(terms)
      print sum, "= sum of", len(terms), "terms:", terms


exit()
  
# this approach is O(n^3), does not realyl fly 
max_t = 0
for p in primes:
  for i in range(0,len(primes)):
    if i > p/2: break
    sum = 0
    terms = []
    for q in primes[i:]:
      sum += q
      terms.append(q)
      if sum == p:
        if len(terms) > max_t:
          max_t = len(terms)
          print p, "= sum of", len(terms), "terms:", terms
      if q > p: break
    