function f = facuft_basecase(x)
N=length(x);
Wn=exp(-2*pi*1i/N);

if N<=2^3
  f= dft(x);
else
  x_even=x(1:2:end); 
  x_odd=x(2:2:end);
  f_even=facuft(x_even);
  f_odd=facuft(x_odd);
  
  f=zeros(size(x));
  first_half_indices=0:(N/2-1);
  twiddle_factors=Wn.^first_half_indices;
  f(1:(N/2))=f_even + twiddle_factors .* f_odd;
  f((N/2+1):end)=f_even - twiddle_factors .* f_odd;
end