p = 1
for i in range(1,101): p *= i
sum = 0
while p!=0:
    sum += p%10
    p = p/10

print sum

