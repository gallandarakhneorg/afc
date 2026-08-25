% Let v and s two vectors:
%
% proj(v) on s = ((v . s) / (s . s)) * s 

output_precision(16)

x = 2
y = 150

x1 = 0
y1 = 0
x2 = -3
y2 = 1

V = [ x-x1, y-y1 ]
S = [ x2-x1, y2-y1 ]

F = ((dot(V,S))/(dot(S,S)))

J = F * S

P = J + [ x1, y1 ]

Dist2P1 = norm([ x1, y1 ] - [ x, y ])
Dist2P2 = norm([ x2, y2 ] - [ x, y ])
Dist2P = norm(P - [ x, y ])

