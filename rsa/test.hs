module Test where

import Number
import ToyRSA
import Encoding

import System.Random

--main =  print (map (chr . fromIntegral) encodedMessage)
main =  print keys
message="0123456789"

n=2048
encodedMessage = encodeAndPad n message
decodedMessage = decode n encodedMessage

reEncodedMessage = encode n decodedMessage
reDecodedMessage = decodeAndUnpad n reEncodedMessage

encrypted = encrypt pub message
decrypted = decrypt pr encrypted

initialSeed = mkStdGen 0
keys= generateKeys n initialSeed

getPubK (Keys pub pr) = pub
getPK (Keys pub pr) = pr

(pub,pr) = (getPubK keys, getPK keys)

bits = stringToBits message charSize
padded n = pad n bits charSize 

testMillerRabin n = millerRabin n 1 initialSeed 


