#!/usr/bin/python

def f(n):
    if (n<=1):
        return 1
    else:
       return n*f(n-1)

digits = [0,1,2,3,4,5,6,7,8,9]
#digits = [0,1,2]
ans = []

s = map(f,range(len(digits)-1,-1,-1))
print s

m = 1000000
#m = 4
m -= 1

i = iter(s)
#while (m>0):
while (len(digits)>0):
    ii = i.next()
    (n, m) = divmod(m,ii)
    ans.append(digits[n])
    #print m, n, digits[n],
    del digits[n]
    #print digits

import string
print string.join(map(str,ans),'')
#print "correct: 2783915460"

# cheat from:
# http://blog.dreamshire.com/2009/05/22/project-euler-problem-24-solution/
# 2783915460

