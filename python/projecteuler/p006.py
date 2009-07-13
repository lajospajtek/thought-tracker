def square(n): return n*n

a = range(1,101)
print square(sum(a))-sum(map(square,a))
