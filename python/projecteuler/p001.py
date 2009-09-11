limit = 1000
s = 0
a = []
n = 1
while n<limit:
	if (n%3)==0 or ((n%5)==0):
		s +=n
		a.append(n)
	n+=1

a.sort()
print "sum:", sum(a)
print "sum:", s
#text = raw_input('press enter')
