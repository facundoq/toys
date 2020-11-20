from simplenn.layer import Layer
import numpy as np
from .initializers import  Initializer,Zero,RandomUniform,XavierUniform

class Linear(Layer):

    def __init__(self,input_size:int,output_size:int,initializer:Initializer=RandomUniform(),name=None):
        super().__init__(name=name)
        shape = (input_size,output_size)
        self.w = initializer.create(shape)
        self.register_parameter("w", self.w)


    def forward(self,x:np.ndarray):
        n,d = x.shape
        din,dout = self.w.shape
        assert din==d, f"#features of input ({d}) must match first dimension of W ({din})"
        # calculate output
        self.add_to_cache(x)
        ### COMPLETAR INICIO ###
        y = x.dot(self.w)
        ### COMPLETAR FIN ###
        return y

    def backward(self,δEδy:np.ndarray):
        x, = self.cache

        # Calculate derivative of error E with respect to input x
        # δEδx = δEδy * δyδx = δEδy * w
        # N = number of samples
        # O = output dim
        # I = input dim
        # δEδy.shape = NxO
        # w.shape = IxO
        # δEδx.shape = NxI
        # → δyδx.shape = OxI
        δyδx = self.w.T
        δEδx =δEδy.dot(δyδx)

        # Calculate derivative of error E with respect to parameter w
        # δEδw = δEδy * δyδw = δEδy * x
        δEδw = np.zeros_like(self.w)
        n,d = δEδy.shape
        ## Vectorized

        δEδw = x.T.dot(δEδy)
        # print(x,δEδy,δEδw)
        # for i in range(n):
        #     print(x[i,:].shape,δEδy[i,:].shape)
        #     δEδw_i = δEδy[i,:].dot(x[i,:])
        #     δEδw += δEδw_i
        self.save_gradient("w",δEδw)

        return δEδx



class Bias(Layer):
    '''
    The Bias layer outputs y = x+b, where b is a vector of parameters
    δyδx = [1,1,1,...,1]
    δyδb = [1,1,1,...1]
    '''
    def __init__(self,output_size:int,initializer:Initializer=Zero(),name=None):
        super().__init__(name=name)
        self.b = initializer.create( (output_size,))
        self.register_parameter("b", self.b)

    def forward(self,x:np.ndarray):
        n,d = x.shape
        dout, = self.b.shape
        assert dout==d, f"#features of input ({d}) must match size of b ({dout})"
        y = x+self.b
        self.add_to_cache(x)
        return y

    def backward(self, δEδy:np.ndarray):

        # Calculate derivative of error E with respect to input x
        # δEδx = δEδy * δyδx = δEδy * [1,1,...,1] = δEδy
        δEδx =δEδy

        # Calculate derivative of error E with respect to parameter b
        # δEδb = δEδy * δyδb
        # δyδb = [1, 1, 1, ..., 1]
        n,d = δEδy.shape
        δEδb = np.zeros_like(self.b)
        for i in range(n):
            δEδb_i = δEδy[i,:] # * [1,1,1...,1]
            δEδb += δEδb_i

        self.save_gradient("b",δEδb)

        return δEδx



class Dense(Layer):
    def __init__(self,input_size:int,output_size:int,
                 linear_initializer:Initializer=XavierUniform(),bias_initializer:Initializer=Zero(),name=None):
        self.linear = Linear(input_size,output_size,initializer=linear_initializer)
        self.bias = Bias(output_size,initializer=bias_initializer)
        super().__init__(name=name)

    def forward(self,x:np.ndarray):
        return self.bias.forward(self.linear.forward(x))

    def backward(self,δEδy:np.ndarray):
        return self.linear.backward(self.bias.backward(δEδy))

    def reset(self):
        self.linear.reset()
        self.bias.reset()

    def get_parameters(self):
        p_linear = self.linear.get_parameters()
        p_bias = self.bias.get_parameters()
        p = {**p_linear, **p_bias}
        return p

    def get_gradients(self):
        g_linear = self.linear.get_gradients()
        g_bias = self.bias.get_gradients()
        g = {**g_linear, **g_bias}
        return g


