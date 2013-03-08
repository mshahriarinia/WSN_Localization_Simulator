function ret = getFxx(k, l, gamma, s)

if k == l
    sum = 0;
    for i = 1: n % get neighbors

        if ADJ(k, i) ~= 0

            sum = sum + ((X(k) - X(i)) ^ 2) / d(k, i) ^ s;

        end
    end
    ret = gamma * sum;

else
    if ADJ(k, l) == 0
        ret = 0;
    else
        ret = - gamma * ((X(k) - X(l)) ^ 2) / d(k, l) ^ s;
    end

end

end