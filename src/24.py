from z3 import *

compare_additions = [13, 11, 11, 10, -14, -4, 11, -3, 12, -12, 13, -12, -15, -12]
success_additions = [10, 16, 0, 13, 7, 11, 11, 10, 16, 8, 15, 2, 5, 10]
dividers = [1, 1, 1, 1, 26, 26, 1, 26, 1, 26, 1, 26, 26, 26]

def make_problem(s):
	for w in ws:
		s.add(w >= 1)
		s.add(w <= 9)

	z = 0
	for w, c1, c2, c3 in zip(ws, compare_additions, success_additions, dividers):
		x = z % 26
		z = z / c3
		x = If(x + c1 == w, 0, 1)
		z = z * If(x == 1, 26, 1) + If(x == 1, w + c2, 0)
	s.add(z == 0)

	final_ws = 0
	for w in ws:
		final_ws = final_ws * 10 + w
	return final_ws


def solve(inp, maximize=True):
	s = z3.Optimize()
	problem = make_problem(s)

	if maximize:
		s.maximize(problem)
	else:
		s.minimize(problem)

	s.check()
	return s.model().eval(problem)

ws = [Int(f"w{i}") for i in range(14)]

print(solve(ws, maximize=True))
print(solve(ws, maximize=False))
