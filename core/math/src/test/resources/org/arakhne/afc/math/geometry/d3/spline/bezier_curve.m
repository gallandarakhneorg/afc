function retval = quadraticbezier(t, a, b, c)
  retval = ((1-t).^2) * a + 2 * t * (1-t) * b + (t.^2) * c;
endfunction

function retval = cubicbezier(t, a, b, c, d)
  retval = (1-t).^3 * a + 3*t*(1-t).^2 * b + 3*t.^2*(1-t) * c + t.^3 * d;
endfunction

function retval = quadraticbezierchain(P)
  R = [];
  i = 1;
  while ((i+2) <= rows(P))
    p0 = P(i,:);
    p1 = P(i+1,:);
    p2 = P(i+2,:);
    t = 0;
    while (t <= 1)
      p = quadraticbezier(t, p0, p1, p2);
      R = [R; p];
      t = t + 0.1;
    endwhile
    i = i + 2;
  endwhile
  retval = R;
endfunction

function retval = cubicbezierchain(P)
  R = [];
  i = 1;
  while ((i+3) <= rows(P))
    p0 = P(i,:);
    p1 = P(i+1,:);
    p2 = P(i+2,:);
    p3 = P(i+3,:);
    t = 0;
    while (t <= 1)
      p = cubicbezier(t, p0, p1, p2, p3);
      R = [R; p];
      t = t + 0.1;
    endwhile
    i = i + 3;
  endwhile
  retval = R;
endfunction

p0 = [ 12, 34, 56 ];
p1 = [ 154, 10, 41 ];
p2 = [ 458, 21, 1 ];
p3 = [ 500, 0, 0 ];
p4 = [ 750, -50, 1 ];
p5 = [ 450, 47, -114 ];
p6 = [ -14, -14, -110 ];

#A = [p0; p1; p2; p3; p4];
#qI = quadraticbezierchain(A)

A = [p0; p1; p2; p3; p4; p5; p6];
qI = cubicbezierchain(A)

plot3(A(:,1), A(:,2), A(:,3), ";Control Points;", qI(:,1), qI(:,2), qI(:,3), ";Spline;");

