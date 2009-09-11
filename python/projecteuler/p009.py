# a^2 + b^2 = c^2
# a + b + c = 1000
# -> eliminate c; express b as funtion of a, loop over possible values of a
#    check initial conditions for (a,b,c) 
for a in range(1,500):
    b = (1000000-2000*a)/(2000-2*a) 
    if b != int(b): continue
    c = 1000 - a -b
    if c*c == (a*a + b*b): print a, b, c, a*b*c

