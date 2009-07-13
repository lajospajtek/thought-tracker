# p 049:

from math import sqrt

def is_prime(n):
  return n in four_digit_primes
#  for i in four_digit_primes:
#    if n == i: return True
#  return False

def print_prime_status(m):
  print m, "is",
  if not is_prime(m): print "not",
  print "prime"

def fingerprint(p):
    fp = 0
    while p>0:
        d = p%10
        fp += 10**d
        p /= 10
    return fp

def same_digits(p1, p2):
    return fingerprint(p1) == fingerprint(p2)

primes=[2,3]
for i in range(5,9999,2):
  l = int(sqrt(i))
  for d in primes[1:l+1]:
    if i % d == 0: break
  else: primes.append(i)

i = 0
while i<len(primes):
  if primes[i] > 999: break
  i += 1

four_digit_primes = primes[i:]
#print four_digit_primes
#print_prime_status(1487)
#print_prime_status(1488)

for p1 in four_digit_primes:
    for i in range(2,(10000-2*p1),2):
        p2 = p1 + i
        if is_prime(p2) and same_digits(p1, p2): 
            p3 = p2 + i
            if p3>9999: break
            #print p1, p2, p3, "(", i, ")"
            if is_prime(p3) and same_digits(p1, p3):
            #if is_prime(p3):
                print "!!!!", p1, p2, p3, "(", i, ")"


# 1487 4817 8147 ( 3330 )
# 2969 6299 9629 ( 3330 )

