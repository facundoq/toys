function f = facuft(x)
N=length(x);%O(N)
Wn=exp(-2*pi*1i/N);%O(1)

if N==1
  f= x; %O(1)
else
  x_even=x(1:2:end);%O(N) 
  x_odd=x(2:2:end); %O(N)
  f_even=facuft(x_even);% T(N/2)
  f_odd=facuft(x_odd);%T(N/2)
  
  f=zeros(size(x));%O(N)
  first_half_indices=0:(N/2-1);%O(N)
  twiddle_factors=Wn.^first_half_indices;%O(N)
  f(1:(N/2))=f_even + twiddle_factors .* f_odd;%O(N)
  f((N/2+1):end)=f_even-twiddle_factors.*f_odd;%O(N)
end