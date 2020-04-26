import abc

def reverse_dict(d:dict):
    return {v: k for k, v in d.items()}

default_valid_chars = "!. abcdefghijklmnopqrstuvwxyz"

class Encoder(abc.ABC):
    @abc.abstractmethod
    def encode(self,text:str)->[int]:
        pass

    @abc.abstractmethod
    def decode(self,a:[int])->str:
        pass
    
    def values(self)->int:
        pass

class CharEncoder(Encoder):
    def __init__(self,valid_chars=default_valid_chars,ignore_missing=True):
        self.ignore_missing=ignore_missing

        self.valid_char = valid_chars

        self.id_to_char=dict(enumerate(self.valid_char))
        self.char_to_id= reverse_dict(self.id_to_char)

    def values(self):
        return len(self.valid_char)

    def encode_char(self,c:chr):
        if not c in self.valid_char:
            raise ValueError(f"Invalid char {c} with code {ord(c)}")
        return self.char_to_id[c]

    def decode_char(self,n:int):
        return self.id_to_char[n]

    def remove_missing(self,text:str)->str:
        return filter(lambda c: c in self.valid_char,text)

    def encode(self,text:str)->[int]:
        if self.ignore_missing:
            text = self.remove_missing(text)
        return list(map(self.encode_char,text))

    def decode(self,text:[int])->str:
        return "".join(map(self.decode_char,text))



#from nltk import word_tokenize

#import nltk 
# nltk.download('punkt')

# def parse(text:[str]):
#     result=[]
#     for line in text:
#         tokens = word_tokenize(line)
#         result.extend(tokens)
#         result.append("\n")
#     return result

def text2words(text:str,delimiter:str):
    lines = text.split("\n")
    for line in lines:
        words = line.split(delimiter)
        for w in words:
            yield w
        yield("\n")

class WordEncoder(Encoder):

    @classmethod
    def fit(cls,text:str,min_appearances=1,delimiter=" "):
        word_count=dict()
        word_count["\n"]=min_appearances
        for word in text2words(text,delimiter):
            count = word_count.get(word,0)
            count+=1
            word_count[word]=count
        words = [w for w,c in word_count.items() if c>=min_appearances]            
        
        return WordEncoder(words,delimiter)

    def __init__(self,words:[str],delimiter:str):
        self.id_to_word=dict(enumerate(words))
        self.word_to_id= reverse_dict(self.id_to_word)
        self.delimiter=delimiter

    def values(self):
        return len(self.id_to_word)

    def encode_word(self,s:str)->int:
        return self.word_to_id[s]

    def decode_word(self,n:int)->str:
        return self.id_to_word[n]
    
    def can_encode(self,s:str)->bool:
        return s in self.word_to_id

    def encode(self,text:str)->[int]:
        text = text2words(text,self.delimiter)
        text = filter(self.can_encode,text)
        return list(map(self.encode_word,text))

    def decode(self,text:[int])->str:
        return self.delimiter.join(map(self.decode_word,text))