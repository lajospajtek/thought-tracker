p = 1
for i in range(0,1000): p *= 2
sum = 0
while p!=0:
    sum += p%10
    p = p/10

print sum

