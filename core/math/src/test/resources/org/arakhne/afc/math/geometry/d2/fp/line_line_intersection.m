% 1) Matrix representation of an equations system
% -----------------------------------------------
%
%   a.x + b.y = c
%   d.x + e.y = f
%
%   The matrix representation of the equation system is:
%   A = [ a  b ] 
%       [ d  e ]
%
%   b = [ c ]
%       [ f ]
%
%   A \ b = [ x ]
%           [ y ]
%
%
% 2) Line intersection problem
% ----------------------------
%
%   The equations of the lines are:
%   P1 + s.(P2-P1) = I1 = I
%   P3 + t.(P4-P3) = I2 = I
%
%   x1 + s.(x2-x1) = i
%   y1 + s.(y2-y1) = j
%   x3 + t.(x4-x3) = i
%   y3 + t.(y4-y3) = j
%
%   x1 + s.(x2-x1) = x3 + t.(x4-x3)
%   y1 + s.(y2-y1) = y3 + t.(y4-y3)
%
%   s.(x2-x1) - t.(x4-x3) = x3-x1
%   s.(y2-y1) - t.(y4-y3) = y3-y1
%
%   (x2-x1).s + (x3-x4).t = x3-x1
%   (y2-y1).s + (y3-y4).t = y3-y1
%
%   A = [ (x2-x1)  (x3-x4) ]
%       [ (y2-y1)  (y3-y4) ]
%
%   b = [ x3-x1 ]
%       [ y3-y1 ]
%
%   R = A \ b = [ s ]
%               [ t ]
%
%   [ x ] = P1 + s * (P2 - P1) = [ x1 ] + s * [ x2-x1 ] 
%   [ y ]                        [ y1 ]       [ y2-y1 ]
%   or
%   [ x ] = P3 + t * (P4 - P3) = [ x3 ] + t * [ x4-x3 ] 
%   [ y ]                        [ y3 ]       [ y4-y3 ]

output_precision(16)

x1 = 0
y1 = 0
x2 = 3
y2 = -2

x3 = -3
y3 = -1
x4 = 4
y4 = -1

A = [ x2-x1, x3-x4; y2-y1, y3-y4 ]
b = [ x3-x1; y3-y1 ]
R = A \ b
s = R(1)
t = R(2)
I1 = [ double(x1 + s * (x2-x1)), double(y1 + s * (y2-y1)) ]
I2 = [ double(x3 + t * (x4-x3)), double(y3 + t * (y4-y3)) ]

Iavg = (I1 + I2) / 2


