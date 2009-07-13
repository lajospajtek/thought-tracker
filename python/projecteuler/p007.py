n_max = 10001

primes = [2, 3, 5, 7, 11]
i = primes[-1]
while len(primes) < n_max:
    i += 2
    for d in primes:
        if i % d == 0: break
        ## the 'optimized' vestion below performs worse for the given n_max
        #div, rem = divmod(i, d)
        #if rem == 0: break
        #if d > div: break
    else:
        primes.append(i)

#print primes
print primes[-1]


