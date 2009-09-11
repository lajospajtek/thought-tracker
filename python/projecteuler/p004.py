def check_palindrome(n):
    a = []
    while n !=0:
        n, d = divmod(n, 10)
        a.append(d)
    i, j = 0, len(a)-1
    while i < j:
        if (a[i] != a[j]): break
        i += 1
        j -= 1
    else:
        return True
    return False
	
mp = 0
for i in range(999, 99, -1):
    for j in range(999, 99, -1):
        n = i*j
        if n<mp: break
        if check_palindrome(n):
            print i, j, n
            if n>mp: mp=n

print mp
