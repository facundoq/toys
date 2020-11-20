from simplenn.layer import Layer
import numpy as np


class ReLU(Layer):

    def forward(self,x:np.ndarray):
        y = np.zeros_like(x)

        # TIP: NO utilizar np.max()
        # Ya que devuelve el valor máximo
        # y no aplica la función elemento a elemento

        ### COMPLETAR INICIO ###
        y = np.maximum(x,0)
        ### COMPLETAR FIN ###

        self.add_to_cache(y)
        return y

    def backward(self, δEδy:np.ndarray):
        δEδx = np.zeros_like(δEδy)
        y, = self.cache

        # TIP: δEδx = δEδy * δyδx
        # δyδx is 1 if the output was greater than 0
        # and 0 otherwise

        ### COMPLETAR INICIO ###
        δyδx = y>0
        δEδx = δEδy * δyδx
        ### COMPLETAR FIN ###

        return δEδx


class Sigmoid(Layer):

    def forward(self,x:np.ndarray):
        y = np.zeros_like(x)
        ### COMPLETAR INICIO ###
        y =   1.0/(1.0 + np.exp(-x))
        ### COMPLETAR FIN ###
        self.add_to_cache(y)
        return y

    def backward(self, δEδy:np.ndarray):
        δEδx= np.zeros_like(δEδy)
        y, = self.cache
        # TIP: δEδx = δEδy * δyδx
        # First calculate δyδx
        # then multiply by δEδy (provided)

        ### COMPLETAR INICIO ###
        δyδx = y * (1.0-y)
        δEδx = δEδy * δyδx
        ### COMPLETAR FIN ###

        return δEδx



class TanH(Layer):
    def __init__(self,name=None):
        self.sigmoid=Sigmoid()
        super().__init__(name=name)


    def forward(self,x:np.ndarray):
        y= np.zeros_like(x)
        # TIP: TanH is simply sigmoid*2-1
        ### COMPLETAR INICIO ###
        y=self.sigmoid.forward(x)*2-1
        ### COMPLETAR FIN ###
        return y

    def backward(self,δEδy:np.ndarray):
        δEdx= np.zeros_like(δEδy)
        # TIP: If TanH is simply sigmoid*2-1
        # Calculate derivative of TanH
        # in terms of derivative of sigmoid
        ### COMPLETAR INICIO ###
        δEdx=self.sigmoid.backward(δEδy)*2
        ### COMPLETAR FIN ###
        return δEdx

    def get_gradients(self):
        return self.sigmoid.get_gradients()

    def reset(self):
        self.sigmoid.reset()


class MultiplyConstant(Layer):
    '''
    A layer that multiplies by a constant
    This layer has NO parameters
    '''
    def __init__(self,value:float,name=None):
        super().__init__(name=name)
        self.value=value
    def forward(self,x:np.ndarray):
        '''
        :param x: input vector/matrix
        :return: x * a constant value, stored in self.value
        '''
        y= np.zeros_like(x)
        ### COMPLETAR INICIO ###
        y=x*self.value
        ### COMPLETAR FIN ###
        return y


    def backward(self,δEδy:np.ndarray):
        δEδx= np.zeros_like(δEδy)
        ### COMPLETAR INICIO ###
        δEδx=δEδy *self.value
        ### COMPLETAR FIN ###
        return δEδx

class AddConstant(Layer):
    '''
    A layer that adds a constant
    This layer has NO parameters
    '''

    def __init__(self,value:float,name=None):
        super().__init__(name=name)
        self.value=value

    def forward(self,x:np.ndarray):
        '''
        :param x: input vector/matrix
        :return: x + a constant value, stored in self.value
        '''
        y= np.zeros_like(x)
        ### COMPLETAR INICIO ###
        y=x+self.value
        ### COMPLETAR FIN ###
        return y

    def backward(self,δEδy:np.ndarray):
        δEδx= np.zeros_like(δEδy)
        ### COMPLETAR INICIO ###
        δEδx= np.ones_like(δEδy)
        ### COMPLETAR FIN ###
        return δEδx




class Softmax(Layer):
    def forward(self,x:np.ndarray):
        n,classes=x.shape
        y = np.zeros_like(x)
        for i in range(n):
            xi=x[i,:]
            xi -= xi.max() # trick to avoid numerical issues
            # Calcular las probabilidades para cada clase
            # y guardar el valor en y[i,:]
            ### COMPLETAR INICIO ###
            e = np.exp(xi)
            y[i,:] = e/e.sum()
            ### COMPLETAR FIN ###
        self.add_to_cache(y)
        return y
    def backward(self, δEδy:np.ndarray):
        # δEδx = δEδy * δyδx
        y, = self.cache
        n,classes = δEδy.shape
        δEδx = np.zeros_like(δEδy)
        for i in range(n):
            δEδx[i,:]= self.backward_sample(δEδy[i,:],y[i,:])
        return δEδx


    def backward_sample(self,δEδy:np.ndarray,y:np.ndarray):
        # AYUDA PARA EL CÁLCULO
        # http://facundoq.github.io/guides/softmax_derivada.html
        '''
        :param δEδy: derivative of error wrt output for a *single sample*
        :param y: output for a *single sample*
        :return: δEδx for a *single sample*
        '''
        δEδx = np.zeros_like(δEδy)
        classes=y.shape[0]
        for j in range(classes):
            δyδx_j = self.softmax_derivative(y,j)
            ### COMPLETAR INICIO ###
            δEδx[j] = δEδy.dot(δyδx_j)
            ### COMPLETAR FIN ###

        return δEδx

    def softmax_derivative(self,y:np.ndarray,j:int):
        # AYUDA PARA EL CÁLCULO
        # http://facundoq.github.io/guides/softmax_derivada.html
        '''
        :param y: output for a single sample
        :param j: index of input variable for a single sample
        :return: δyδx_j, ie, derivative of output vector for a single input variable x_j
        '''
        δyδx_j = np.zeros_like(y)
        classes = y.shape[0]
        ### COMPLETAR INICIO ###
        for i in range(classes):
            if i ==j:
                δyδx_j[i] = (1-y[i])*y[i]
            else:
                δyδx_j[i] = -y[j]*y[i]
        ### COMPLETAR FIN ###

        return δyδx_j
