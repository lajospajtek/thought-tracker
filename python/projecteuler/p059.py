# project euler problem 059

def sum(a,b): return a + b

def read_cipher(filename):
    cipher = []
    file = open(filename)
    for line in file:
        numbers = line.strip().split(",")
        if numbers == ['']: break
        cipher.extend(map(int, numbers))
    return cipher

cipher = read_cipher("cipher1.txt")
#print cipher

# hypothesis: spaces have the highest frequency
y = [0]*256
for letter in cipher[0::3]: y[letter] += 1
c1 = 32 ^ y.index(max(y))

y = [0]*256
for letter in cipher[1::3]: y[letter] += 1
c2 = 32 ^ y.index(max(y))

y = [0]*256
for letter in cipher[2::3]: y[letter] += 1
c3 = 32 ^ y.index(max(y))

#print c1, c2 ,c3
print "three char key:", chr(c1), chr(c2), chr(c3)

# todo: more concise expression needed
m = reduce(sum, map(lambda x: x^103, cipher[0::3]))
m += reduce(sum, map(lambda x: x^111, cipher[1::3]))
m += reduce(sum, map(lambda x: x^100, cipher[2::3]))

print "sum of the ASCII values:", m

clear = cipher
clear[0::3] = map(chr, map(lambda x: x^103, cipher[0::3]))
clear[1::3] = map(chr, map(lambda x: x^111, cipher[1::3]))
clear[2::3] = map(chr, map(lambda x: x^100, cipher[2::3]))
print "".join(clear)


