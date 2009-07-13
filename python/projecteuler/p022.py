f = open("names.txt", "r")
text = f.read()
names = text.split("\",\"")
names[0] = names[0][1:]
names[-1] = names[-1][:-1]
names.sort()
total = 0
n = 1
for name in names:
    nv = 0
    for letter in name: nv += (ord(letter)-64)
    total += n*nv
    n += 1
print total

def sum(a,b): return a+b

def letter_value(s):
    return reduce(sum, map(lambda x: ord(x)-64, s))

n = 1
total = 0
for name in names:
    total += n*letter_value(name)
    n += 1
print total
