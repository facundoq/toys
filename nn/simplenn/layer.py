from abc import ABC,abstractmethod
import numpy as np

layer_counter = {}

class Layer(ABC):

    def __init__(self,name=None):
        if name is None:
            class_name= self.__class__.__name__
            count=layer_counter.get(class_name,0)
            name =f"{class_name}_{count}"
            layer_counter[class_name]=count+1
        self.name=name
        self.parameters = {}
        self.reset()
        self.frozen=False

    def n_parameters(self)->int:
        parameters=0
        for p in self.get_parameters().values():
            parameters+=p.size
        return parameters

    def register_parameter(self,name,value):
        self.parameters[name]=value

    def save_gradient(self,name,value):
        assert name in self.parameters.keys()
        self.gradients[name] = value

    def get_parameters(self):
        return self.parameters

    def get_gradients(self):
        return self.gradients

    def reset(self):
        self.cache = ()
        self.gradients = {}

    def add_to_cache(self,v:np.ndarray):
        self.cache = (*self.cache,v)

    @abstractmethod
    def forward(self,x:np.ndarray):
        pass


    @abstractmethod
    def backward(self,δEδy:np.ndarray):
        pass

    def __repr__(self):
        return f"{self.name}"

class ErrorLayer(ABC):


    @abstractmethod
    def forward(self,y:np.ndarray,y_pred:np.ndarray)->float:
        pass


    @abstractmethod
    def backward(self,y:np.ndarray,y_pred:np.ndarray):
        pass


class SampleErrorLayer(ABC):


    @abstractmethod
    def forward(self,y:np.ndarray,y_pred:np.ndarray)->np.ndarray:
        pass


    @abstractmethod
    def backward(self,y:np.ndarray,y_pred:np.ndarray):
        pass

