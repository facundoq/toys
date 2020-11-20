from .layer import SampleErrorLayer,ErrorLayer,Layer
from .activations import  Sigmoid,TanH,ReLU,MultiplyConstant,AddConstant,Softmax
from .dense import Linear,Dense,Bias
from .error_layer import SquaredError,MeanError,CrossEntropyWithLabels,BinaryCrossEntropyWithLabels
from .model import Sequential,Model
from .optimizer import RandomOptimizer,GradientDescent


eps = 1e-12