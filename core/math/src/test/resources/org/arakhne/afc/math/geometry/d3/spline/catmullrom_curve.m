function retval = catmullrom(t, G, P0, P1, P2, P3)
  U = [ 1, t, t**2, t**3 ];
  Ps = U * G * [P0; P1; P2; P3 ];
  retval = Ps;
endfunction

function retval = catmullromseg(G, p0, p1, p2, p3)
    R = [];
    t = 0;
    while (t <= 1)
      p = catmullrom(t, G, p0, p1, p2, p3);
      R = [R; p];
      t = t + 0.1;
    endwhile
    retval = R;
endfunction

function retval = catmullromchain(theta, P)
  # Geometric matrix
  G = [	0		1		0		0
	-theta		0		theta		0
	2*theta		theta-3		3-2*theta	-theta
	-theta		2-theta		theta-2		theta ];
  #
  R = [];
  # Put the first point two times for starting the curve from it.
  p0 = P(1,:);
  p1 = p0;
  p2 = P(2,:);
  p3 = P(3,:);
  p = catmullromseg(G, p0, p1, p2, p3);
  R = [R; p];
  # Loop on the internal segments
  i = 2;
  while (i <= (rows(P) - 2))
    p0 = P(i-1,:);
    p1 = P(i,:);
    p2 = P(i+1,:);
    p3 = P(i+2,:);
    p = catmullromseg(G, p0, p1, p2, p3);
    R = [R; p];
    i = i + 1;
  endwhile
  # Put the last point two times for ending the curve to it.
  p3 = P(rows(P),:);
  p2 = p3;
  p1 = P(rows(P)-1,:);
  p0 = P(rows(P)-2,:);
  p = catmullromseg(G, p0, p1, p2, p3);
  R = [R; p];
  retval = R;
endfunction

p0 = [ 12, 34, 56 ];
p1 = [ 154, 10, 41 ];
p2 = [ 458, 21, 1 ];
p3 = [ 500, 0, 0 ];
p4 = [ 750, -50, 1 ];
p5 = [ 450, 47, -114 ];
p6 = [ -14, -14, -110 ];

A = [p0; p1; p2; p3; p4; p5; p6];
qI = catmullromchain(0.5, A);

qI

plot3(A(:,1), A(:,2), A(:,3), ";Control Points;",
      qI(:,1), qI(:,2), qI(:,3), ";Spline 0.5;");

