# bute force approach

from math import sqrt

rng = range(0,10000)
d = {}
for i in rng:
  sum = 1
  for j in range (2,int(sqrt(i)+1)):
    if i % j == 0: sum += (j + i/j) 
  d[i] = sum

#print d
print "---"
ans = 0
for i in rng:
  for j in d:
    if i!=j and i == d[j] and j == d[i]: 
      ans += i
      print i, j

print ans

# 31626