# 
# 
max_len = -1
d = -1
for i in range(3,1000,2):
	print i, 1.0/i,
	dv = 0
	j = 1
	digits = []
	while dv == 0: # remove leading zeroes
		dv, rm = divmod(j,i)
		j = 10 * rm
	if rm != 0: digits.append([dv,rm])
	while True:
		dv, rm = divmod(j,i)
		if rm == 0: break # exact representation 
		if [dv, rm] in digits: break # div-remainder pair seen before: cycle
		j = 10 * rm
		digits.append([dv,rm])
	c = len(digits)
	if c > max_len: max_len, d = c, i
	print "(",len(digits),")"
print "d =", d, "with max reccurent cycle =", max_len
