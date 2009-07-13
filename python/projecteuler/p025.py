digits = 1000
lim = 10**(digits-1)-1
a, b = 1, 1
n = 2
while b<lim:
    a, b = b, a+b
    n += 1

#print a
#print b
print n
