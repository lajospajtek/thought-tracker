sum = 0
for i in range(1,1001):
    sum += i**i

print str(sum)[-10:]

#or:

print str(reduce(lambda x,y:x+y, map(lambda x:x**x, range(1,1001))))[-10:]

