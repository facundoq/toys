{-
TODO:
- isPrime

- Encode
  Convert STRING to bit vector
  pad with 0's till the end to get a multiple of bits(N) bits
  divide as a list of bitvectors of length bits(N)
  Convert each bitvector into an Integer (optional)

- Decode
  Convert STRING to bit vector
  pad with 0's till the end to get a multiple of bits(N) bits
  divide as a list of bitvectors of length bits(N)
  Convert each bitvector into an Integer (optional)


-}

module ToyRSA where

import Debug.Trace

import Data.BitVector -- cabal install bv-0.1.0
import Data.Char
import System.Random

import Number
import Encoding


type Key = Integer
type BitsDesired = Int
data PublicKey = PubK Key Modulo BitsDesired deriving (Show,Eq)
data PrivateKey = PK Key PublicKey  deriving (Show,Eq)

data KeyPair = Keys PublicKey PrivateKey deriving (Show,Eq)

getPubK (Keys pub pr) = pub
getPK (Keys pub pr) = pr



-- Generate a key pair with the desired bits using the a seed
-- bitsDesired must be a multiple of charSize, which is currently 8=2^3 to encode ASCII-encoded text
generateKeys :: BitsDesired -> Seed -> KeyPair
generateKeys bitsDesired seed = Keys puk prk
              where  (q, seed') = generateRandomPrimeMillerRabin 10 r seed
                     (p, seed'') = generateRandomPrimeMillerRabin 10 r seed'
                     n = p*q
                     totient = (p-1) * (q-1)
                     (a,seed''') = generateRandomWithInverse totient r seed''
                     b = positiveModularInverse a totient
                     puk = PubK a n bitsDesired
                     prk = PK b puk
                     r = rangeForBits (div bitsDesired 2)

rangeForBits n = (2^(n-1),2^(n+1))

-- 1) Encode the message s to make it's size a multiple of the bits desired, and transform the result into an array of numbers
-- 2) Encrypt the result with public key
-- 3) Decode the result, transforming it again into a String
encrypt :: PublicKey -> String -> String
encrypt p@(PubK a n bitsDesired) s = decode bitsDesired $ encryptIntegers p $ encodeAndPad bitsDesired s

-- Performs the actual encryption
encryptIntegers :: PublicKey -> [Integer] -> [Integer]
encryptIntegers (PubK a n bitsDesired) xs = map (modularPower a n) xs


-- 1) Encode the message s to transform it into an array of numbers
-- 2) Encrypt the result with public key
-- 3) Decode the result, first removing the pad previously added, and then transforming it again into a String
decrypt :: PrivateKey -> String -> String
decrypt p@(PK b (PubK a n bitsDesired)) s = decodeAndUnpad bitsDesired $ decryptIntegers p $ encode bitsDesired s

-- Performs the actual decryption 
decryptIntegers :: PrivateKey -> [Integer] -> [Integer]
decryptIntegers (PK b (PubK a n bitsDesired)) xs = map (modularPower b n) xs

