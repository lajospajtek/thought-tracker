# project euler problem 011
# a quick hack

def read_square(filename):
    square = []
    file = open(filename)
    for line in file:
        numbers = line.strip().split(" ")
        if numbers == ['']: break
        tline = []
        for i in numbers:
            tline.append(int(i))
        square.append(tline)
    return square

def prod(a, b): return a * b

def max_horiz(square, max):
    for l in square:
        for i in range(0, len(l)-3):
            p = reduce(prod, l[i:i+4])
            if p > max: max = p
    return max

def max_vert(s, max):
    for i in range(0,16):
        for j in range(0,19):
            p = s[i][j]*s[i+1][j]*s[i+2][j]*s[i+3][j]
            if p>max: max=p
    return max

def max_diag1(s, max):
    for i in range(0,16):
        for j in range(0,16):
            p = s[i][j]*s[i+1][j+1]*s[i+2][j+2]*s[i+3][j+3]
            if p>max: max=p
    return max

def max_diag2(s, max):
    for i in range(0,16):
        for j in range(3,19):
            p = s[i][j]*s[i+1][j-1]*s[i+2][j-2]*s[i+3][j-3]
            if p>max: max=p
    return max

square = read_square("p011.txt")
max = max_horiz(square, 1)
max = max_vert(square, max)
max = max_diag1(square, max)
max = max_diag2(square, max)
print max

