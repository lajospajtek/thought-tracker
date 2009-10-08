#!/usr/bin/python

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
# 2783915460

