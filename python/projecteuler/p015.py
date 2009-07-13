# proejct euler 0115

n = 20

def move_to(x, y):
    if x>n or y>n: return 0
    if x==n and y==n: return 1
    return move_to(x+1,y) + move_to(x,y+1)

#print move_to(0,0)

# expandin the recursion above, we get to the binomial coefficients  or pascal's triangle
# we are looking for the "middle term" where n = 20+20 and k = 20
# see for instance: http://en.wikipedia.org/wiki/Binomial_coefficient

def fact(n):
    if n<=1: return 1
    return n*fact(n-1)

print fact(2*n) / (fact(n)*fact(n))

