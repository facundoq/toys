function f = dft(x)
  f=zeros(size(x)); % O(N)
  N=length(x); % O(N)
  Wn=exp(-2*pi*1i/N); % O(1) 
  for k=1:N % O(N*N)
    for n=1:N
      f(k)=f(k)+ x(n) * Wn^{(k-1)*(n-1)}  % O(1)
    end
  end
end