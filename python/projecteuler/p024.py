#!/usr/bin/python

# A permutation is an ordered arrangement of objects. For example, 3124 is one 
# possible permutation of the digits 1, 2, 3 and 4. If all of the permutations 
# are listed numerically or alphabetically, we call it lexicographic order. 
# The lexicographic permutations of 0, 1 and 2 are:
#
#     012   021   102   120   201   210
#
# What is the millionth lexicographic permutation of the digits 0, 1, 2, 3, 4, 
# 5, 6, 7, 8 and 9?
#
# Answer: 2783915460

n=10
digits = range(0,n)
m = 1000000

def f(n):
    if (n<=1):
        return 1
    else:
       return n*f(n-1)

m -= 1
ans = []
s = map(f,range(len(digits)-1,-1,-1))
i = iter(s)
while (len(digits)>0):
    ii = i.next()
    (n, m) = divmod(m,ii)
    ans.append(digits[n])
    #print m, n, digits[n],
    del digits[n]
    #print digits

import string
print string.join(map(str,ans),'')

# cheat from:
# http://blog.dreamshire.com/2009/05/22/project-euler-problem-24-solution/

