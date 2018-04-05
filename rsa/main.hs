module Main where

import Number
import ToyRSA
import Encoding

import System.Random


main =  print keys
message="0123456789 Hola Buen dia como le va?" -- Message to encode

-- bits for key (ie, key size). The primes p and q will have n/2 
n=2048 -- note that with 2048 bits, it takes about 10 seconds to generate a key pair.

 -- we are seeding the random number generator with 0, but of course in a production environment this should change
initialSeed = mkStdGen 0

-- 
keys= generateKeys n initialSeed
(pub,pr) = (getPubK keys, getPK keys)

-- Encrypt the message with the public key and save it in variable 'encrypted'
encrypted = encrypt pub message
-- Encrypt the message with the private key and save it in variable 'encrypted'
decrypted = decrypt pr encrypted