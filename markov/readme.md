# Markov Chain models

A simple Markov Chain model to generate text. By choosing the Encoder (WordEncoder and CharEncoder) you can swap between a char-level model and a word-level model. 

The implementation can use either scipy's sparse matrices or python dicts to store the frequency matrix of ngrams.

Note that the text preprocessing for both the word encoder is very basic, and does not separate stops, commas, hypens, etc.

Sample output, trained with 3 states (2 previous + 1predicted) 70mb of text (14930222 distinct 3-grams), and generated using a laplacian smoothing of 1e-16:
    
` Who take the old days " notice , and Finest Sea Salt , Paprika ( Bell Pepper ) , and logging , it will be able to obtain access to the east and the world , accept that the policies we must enter the aerospace engineering degrees -- unprecedented mobility for African American adults who were in front of the changes in property damage along New Jersey as it exists , and A. ticks were most likely scenario is based on the threefold workings of the actors know it was 
`