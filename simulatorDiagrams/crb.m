%n number of unknown sensors
%X[n]
%Y[n]
%ADJ[n][n]

Fxx = zeros(n,n);
Fxy = zeros(n,n);
Fyy = zeros(n,n);



s = 4;

np = 3; % 2 < np < 4
sigmaDB = 5; % 4 < sigmaDB < 12
gammaRSS = ((10 * np) / (sigmaDB * log2(10))) ^ 2;
gamma = gammaRSS;

for k = 1: n

    for l = 1: n

        Fxx(k, l) = getFxx(k, l, gamma, s);
        Fxy(k, l) = getFxy(k, l, gamma, s);
        Fyy(k, l) = getFyy(k, l, gamma, s);

    end

end

%FIM
Ftr = [Fxx Fxy
    Fxy' Fyy];




