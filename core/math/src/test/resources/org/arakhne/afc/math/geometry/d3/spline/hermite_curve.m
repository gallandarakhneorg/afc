function retval = hermite(t, p0, m0, p1, m1)
  retval = (2*t**3 - 3*t**2 + 1) * p0 + (t**3 - 2*t**2 + t) * m0 + (-2*t**3 + 3*t**2) * p1 + (t**3 - t**2) * m1;
endfunction

function retval = hermiteseg(p0, m0, p1, m1)
    R = [];
    for t = 0:0.1:0.9
      p = hermite(t, p0, m0, p1, m1);
      R = [R; p];
    endfor
    retval = R;
endfunction

function retval = hermitechain(P, T)
  #
  R = [];
  # Loop on the segments.
  i = 1;
  while (i <= (rows(P) - 1))
    p0 = P(i,:);
    m0 = P(i,:);
    p1 = P(i+1,:);
    m1 = P(i+1,:);
    p = hermiteseg(p0, m0, p1, m1);
    R = [R; p];
    i = i + 1;
  endwhile
  R = [R; P(rows(P),:)];
  retval = R;
endfunction

function retval = cardinalsplinetangents(tension, P)
  t = (P(2,:) - P(1,:)) * (1 - tension) * 0.5;
  T = [t];
  i = 2;
  while (i < rows(P))
    t = (P(i+1,:) - P(i-1,:)) * (1 - tension);
    T = [T; t];
    i = i + 1;
  endwhile
  t = (P(rows(P),:) - P(rows(P)-1,:)) * (1 - tension) * 0.5;
  T = [T; t];
  retval = T;
endfunction

p0 = [ 12, 34, 56 ];
p1 = [ 154, 10, 41 ];
p2 = [ 458, 21, 1 ];
p3 = [ 500, 0, 0 ];
p4 = [ 750, -50, 1 ];
p5 = [ 450, 47, -114 ];
p6 = [ -14, -14, -110 ];

PTS = [p0; p1; p2; p3; p4; p5; p6];

TANGENTS = cardinalsplinetangents(0.25, PTS)

qI = hermitechain(PTS, TANGENTS);

plot3(PTS(:,1), PTS(:,2), PTS(:,3), ";Control Points;",
      qI(:,1), qI(:,2), qI(:,3), ";Spline tension 0.25;");

save output.out qI;

