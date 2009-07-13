#

def seq_len(n):
    len = 1
    while n != 1:
        if n % 2 == 0: n = n / 2
        else: n = 3 * n + 1
        len += 1
    return len

max = 0
idx = 0
for i in range(1,1000000):
    c = seq_len(i)
    if c > max: 
        max = c
        idx = i
        print "!!", idx, max

print idx, max
