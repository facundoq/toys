
import numpy as np
from numpy  import random
import abc
from typing import Dict,List
from tqdm import tqdm

class MarkovChain(abc.ABC):
    
    def __init__(self,states:int,values:int):
        self.states=states
        self.values=values

    @abc.abstractmethod
    def probability(self,sequence:[int]):
        # returns probability of each value conditioned on this sequence
        pass

    def sample(self,start_sequence:[int],length:int,smoothing:float=1e-32):
        # take the last self.states items of the starting sequence
        # disregard the rest
        sequence=start_sequence[-(self.states):]
        generated=[]
        # generate `length` values
        for i in range(length):
            probabilities=self.probability(sequence)+smoothing
            probabilities/=probabilities.sum()
            new_char=random.choice(self.values,p=probabilities)
            generated.append(new_char)
            sequence=sequence[1:]+[new_char]
        return generated
        

from scipy.sparse import dok_matrix


class MarkovChainTable(MarkovChain):
    
    @classmethod
    def fit(cls,sequence:[int],states:int,values:int):
        # Fit a model a model with `states` previous states, where each state can have `values` values.
        # count stores the number of times an ngram of length states+1 appears

        counts=dok_matrix((values**states,values),dtype=np.uint64)
        n = len(sequence)
        ndim_shape=(values,)*states
        # iterate over each subsequence of length = states+1
        # update count(w | w-1 w-2 ... w-states)
        # use tqdm for the progress bar
        for i in tqdm(range(n-(states+1)),miniters=10000,mininterval=1):
            # obtain indices of subsequence
            state_indices=sequence[i:i+states]
            # obtain flat index for subsequence on which the last state is conditioned
            flat_state_index=np.ravel_multi_index(state_indices,ndim_shape)
            # obtain the subsequence index (last item)
            value_index=sequence[i+states]
            # update the count for this ngram
            counts[flat_state_index,value_index]+=1
        return MarkovChainTable(counts,states)

    def __init__(self,counts:np.array,states:int):
        states = states
        values = counts.shape[1]
        super().__init__(states,values)
        self.ndim_shape = ndim_shape=(values,)*states
        self.counts=counts

    def probability(self, sequence:[int]):
        flat_state_index=np.ravel_multi_index(sequence,self.ndim_shape)
        #indices=(*sequence,slice(None))
        count = self.counts[flat_state_index,:]
        count = count.toarray()[0,:]
        return count
    
    def sequences(self)->int:
        return len(self.counts.keys())

class MarkovChainDict(MarkovChain):
    

    @classmethod
    def fit(cls,text:[int],states:int,values:int):
        # Fit a model a model with `states` previous states, where each state can have `values` values.
        
        model=dict()
        n = len(text)
        # iterate over each subsequence of length = states+1
        for i in tqdm(range(n-(states+1)),miniters=10000,mininterval=1):
            key=tuple(text[i:i+states])
            value=text[i+states]
            if not key in model.keys():
                model[key]=np.zeros((values),dtype=np.uint16)    
            model[key][value]+=1
        return MarkovChainDict(states,values,model)

    def __init__(self,states:int,values:int,counts:Dict[List[int],np.ndarray]):
        super().__init__(states,values)
        self.counts=counts # matrix of n-gram counts
        self.unknown_probability=np.zeros((self.values))

    def probability(self, sequence:[int]):
        key=tuple(sequence)
        return self.counts.get(key,self.unknown_probability)
    
    def sequences(self)->int:
        return len(self.counts)