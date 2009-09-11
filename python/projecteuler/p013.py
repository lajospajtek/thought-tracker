def read(filename):
    sum = 0
    file = open(filename)
    for line in file:
        nr = int(line)
        sum += nr
    return str(sum)[0:10]

print read("p013.txt")

