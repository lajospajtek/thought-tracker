# project euler problem 018

def read_triangle(filename):
    triangle = []
    file = open(filename)
    for line in file:
        numbers = line.strip().split(" ")
        if numbers == ['']: break
        tline = []
        for i in numbers: tline.append(int(i))
        triangle.append(tline)
    return triangle

def max_sum_route(triangle):
    for i in range(len(triangle)-1,-1,-1):
        for j in range(0,len(triangle[i])-1):
            if triangle[i][j]>triangle[i][j+1]: triangle[i-1][j] += triangle[i][j]
            else: triangle[i-1][j] += triangle[i][j+1]
    return triangle[0][0]

#triangle = [[3],[7,5],[2,4,6],[8,5,9,3]]
triangle = read_triangle("p018.txt")
print max_sum_route(triangle)

