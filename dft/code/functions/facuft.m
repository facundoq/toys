function y=facuft(x)
%% x must be a column or row vector
%% length(x) must be a power of 2

N=length(x);
Wn=exp(-2*pi*1i/N);

if N==1
    y= x;
else
    % Although x_even contains elements with odd indices of the vector x
    % given that MATLAB's arrays are 1-based, they are actually even
    % indices of the signal. Viceversa for x_odd.
    x_even=x(1:2:end); 
    x_odd=x(2:2:end);
    y_odd=facuft(x_odd);
    y_even=facuft(x_even);
    y=zeros(size(x));
    first_half_indices=0:(N/2-1);
    twiddle_factors=Wn.^first_half_indices;
    y(1:(N/2))=y_even + twiddle_factors .* y_odd;
    y((N/2+1):end)=y_even - twiddle_factors .* y_odd;
end