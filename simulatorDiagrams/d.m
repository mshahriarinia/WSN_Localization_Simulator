function d1 = d(i, j)
%gives the euclidean distance of the two nodes specified by the indexes
load X;
load Y;
d1 = (X(i) - X(j)) ^ 2 + (Y(i) - Y(j)) ^ 2;