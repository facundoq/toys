import numpy as np
from tqdm.auto import tqdm
from simplenn.layer import Layer,ErrorLayer
import abc
from simplenn.optimizer import Optimizer
import random
class Model(abc.ABC):
    '''
    Abstract class model
    '''
    @abc.abstractmethod
    def predict(self,):
        pass

    @abc.abstractmethod
    def fit(self,):
        pass



def batch_arrays(batch_size:int,*arrays,shuffle=True):
    '''

    :param batch_size: size of batches
    :param arrays: variable number of numpy arrays
    :return: a generator that returns the arrays in batches
    '''

    sample_sizes = [a.shape[0] for a in arrays]
    assert all_equal(sample_sizes)
    batches=sample_sizes[0]//batch_size
    batch_list = list(range(batches))
    if shuffle:
        random.shuffle(batch_list)
    for i in batch_list:
        start = i*batch_size
        end = start+batch_size
        batch = [ a[start:end,:] for a in arrays]
        yield tuple(batch)



def all_equal(list:[]):
    return len(list) == 0 or list.count(list[0]) == len(list)

class Sequential(Model):
    '''
        Models a neural network with a sequential (ie, linear) topology
        This network receives as input a single vector x
        And outputs a single vector y
    '''


    def __init__(self, layers:[Layer], error_function:ErrorLayer):
        '''

        :param layers: List of layers, in order
        :param error_function: To be applied to the output of the last layer
        '''
        self.layers:[Layer]=layers
        self.error_function:ErrorLayer=error_function

    def predict(self,x:np.ndarray):
        for layer in self.layers:
            x = layer.forward(x)
        return x

    def fit(self,x:np.ndarray,y:np.ndarray,epochs:int,batch_size:int,optimizer:Optimizer):
        n = x.shape[0]
        batches = n // batch_size
        history = []
        bar = tqdm(range(epochs),desc="fit")
        for epoch in bar:
            epoch_error=0
            for x_batch,y_batch in batch_arrays(batch_size,x,y):
                batch_error=self.fit_batch(x_batch,y_batch,optimizer)
                epoch_error+=batch_error
            epoch_error/=batches
            history.append(epoch_error)
            bar.set_postfix_str(f"error: {epoch_error:.5f}")

        return np.array(history)

    def backward(self,x:np.ndarray,y:np.ndarray):
        '''

        :param x: inputs
        :param y: expected output
        :return: prediction for inputs and error
        '''
        y_pred = self.predict(x)
        E = self.error_function.forward(y,y_pred)
        δEδy = self.error_function.backward(y,y_pred)
        # print(f"E= {E}, δEδy: {δEδy}")
        # print(f", y_pred {y_pred.T} y {y.T}")

        for layer in reversed(self.layers):
            δEδy = layer.backward(δEδy)
            # print(f"{layer}:")
            # print(f"δEδx: {δEδy}")
            # print(f"w: {layer.get_parameters()}")
            # print(f"dw: {layer.get_gradients()}")
        return y_pred, E

    def reset_layers(self):
        for l in self.layers:
            l.reset()

    def fit_batch(self,x:np.ndarray,y:np.ndarray,optimizer:Optimizer):
        self.reset_layers()
        y_pred,error = self.backward(x,y)
        weights = []
        gradients = []

        for layer in self.layers:
            if  layer.frozen:
                continue
            w = layer.get_parameters()
            g = layer.get_gradients()
            # print(f"{layer}:")
            # print(f"g: {g}")
            # print(f"w: {w}")

            weights.append(w)
            gradients.append(g)

        optimizer.optimize(weights,gradients)
        return error


    def summary(self)->str:
        separator = "-------------------------------"
        result=f"{separator}\n"
        parameters=0
        for l in self.layers:
            layer_parameters= l.n_parameters()
            parameters+=layer_parameters
            l_summary=f"{l.name} → params: {layer_parameters}"
            result+=l_summary+"\n"
        result+=f"Total parameters: {parameters}\n"
        result+=f"{separator}\n"
        return result



