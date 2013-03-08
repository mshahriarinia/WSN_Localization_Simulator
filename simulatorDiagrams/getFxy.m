function ret = getFxy(k, l, gamma, s)

if k == l
    sum = 0;
    for i = 1: n % get neighbors

        if ADJ(k, i) ~= 0

            sum = sum + ((X(k) - X(i)) * (Y(k) - Y(i))) / d(k, i) ^ s;

        end
    end
    ret = gamma * sum;

else
    if ADJ(k, l) == 0
        ret = 0;
    else
        ret = - gamma * ((X(k) - X(l)) * (Y(k) - Y(l))) / d(k, l) ^ s;
    end

end

end