from pathlib import Path
from encode import CharEncoder,WordEncoder
from markov_chain import MarkovChainDict,MarkovChainTable,MarkovChain

def read_file_string(filepath:Path):
    with open(filepath) as f:
        content = f.readlines()
        return "\n".join(content)

def read_last_words(filepath:Path):
    words = []
    with open(filepath) as f:
        content = f.readlines()
       
        content = [c.replace("(","") for c in content]
        content = [c.replace(")","") for c in content]
        content = [c.replace(".","") for c in content]
        content = [c.replace(",","") for c in content]
        content = [c.split(" ")[-1] for c in content]
        content = [c[-4:] for c in content]
        print(content[:10])

        return " ".join(content)



if __name__ == "__main__":
    # READ AND ENCODE
    filepath=Path("datasets/hiphop.txt")
    #filepath=Path("datasets/shakespeare_input.txt")
    #filepath=Path("datasets/sherlock_homes.txt")
    text = read_last_words(filepath)
    print(text[:1000])
    #text=text[:10**8] # use a subset of text
    min_appearances=5
    print(f"Generating WordEncoder, using only words which appear at least {min_appearances} times...")
    e= WordEncoder.fit(text,min_appearances=min_appearances)
    print(f"{e.values()} values recognized")
    

    # FIT MODEL
    train_values = e.encode(text)
    states=2
    values = e.values()
    print(f"Fitting model with {states} previous states ({values**(states+1)} possible sequences) ..")
    model=MarkovChainTable.fit(train_values,states,e.values())
    #model=MarkovChainDict.fit(train_values,states,e.values())
    print(f"Model fitted. {model.sequences()} non-zero ngrams out of {model.values**model.states}.")

    # GENERATE
    print("Generating..")
    start_sequence = e.encode("hola cola")
    print(f"Starting text: {e.decode(start_sequence)} ({start_sequence})")
    length=2000
    generated_tokens=model.sample(start_sequence,length,1e-32)
    print(e.decode(generated_tokens))
