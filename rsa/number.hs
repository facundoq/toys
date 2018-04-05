

module Number where

import Debug.Trace
import Data.List (unfoldr)
import Data.Maybe (listToMaybe)
import System.Random

type Modulo = Integer
type Exponent = Integer

type Seed = StdGen
type Range = (Integer,Integer)

gcdx  ::  Integer -> Integer -> (Integer,Integer,Integer)
gcdx a b | a>=b = gcdx' a b
         | otherwise = (d,t,s)
            where (d,s,t)=gcdx' b a


gcdx' a b | r == 0 = (b,0,1)
          | otherwise = (d,t,s-q*t)
           where  r = mod a b
                  q = div a b
                  (d,s,t) = gcdx' b r


divisible a d = mod a d == 0


factorPowers d n | divisible n d = (power+1,number)
                 | otherwise = (0,n)
                 where (power,number) =  factorPowers d (div n d)

-- modularPower :: Integer -> Integer -> Modulo -> Integer
-- modularPower 0 n x = 1
-- modularPower a n x = mod (x* (modularPower (a-1) n x)) n

modularPowerLinear 0 n x = 1
modularPowerLinear a n x = mod ( x * (modularPower (a-1) n x ) ) n

-- x^a = x^(d*2+r) = (x^2)^d+r
modularPower :: Exponent -> Modulo -> Integer ->  Integer
modularPower 0 n x = 1
modularPower a n x | r == 0 = mod call n
                   | otherwise = mod (call * x) n
                 where  (d,r) = divMod a 2
                        call = (modularPower d n x' )
                        x' = mod (x*x) n


modularInverse :: Integer -> Modulo -> Integer
modularInverse a n = t
              where (d,s,t)=gcdx n a

positiveModularInverse a n =  positiveEquivalent inverse n 
                        where inverse = modularInverse a n

-- positive equivalent of a mod n

positiveEquivalent a n | a >= 0 = a
                       | otherwise =(a + difference)
                  where q = abs $ div a n 
                        difference = n * q

randomInteger :: Range -> Seed -> (Integer,Seed)
randomInteger r s = randomR r s



generateRandomNumberUntil :: (Integer -> Bool) -> (Integer,Integer) -> Seed -> (Integer,Seed)
generateRandomNumberUntil p range seed  | p newRandom = (newRandom,seed')
                                       | otherwise = generateRandomNumberUntil p range seed'
                                  where (newRandom,seed') = randomInteger range seed

generateRandomNumber range seed = generateRandomNumberUntil (const True) range seed

generateRandomPrime = generateRandomNumberUntil isPrime
isOdd n = mod n 2 == 1
generateRandomOdd = generateRandomNumberUntil isOdd

generateRandomPrimeMillerRabin k range seed  | result == ProbablyPrime = (newRandom,seed'')
                                        | otherwise = generateRandomPrimeMillerRabin k range seed'
                                  where (newRandom,seed') = generateRandomOdd range seed
                                        (result,seed'')= millerRabin newRandom k seed' 
hasInverseModulo n x = d==1
                      where (d,s,t) = gcdx n x

generateRandomWithInverse n = generateRandomNumberUntil (hasInverseModulo n)




data MillerRabinResult = ProbablyPrime | Composite deriving (Eq,Show)
millerRabin  1  k seed = (ProbablyPrime,seed)
millerRabin  2  k seed = (ProbablyPrime,seed)
millerRabin  n  k seed = millerRabin' (factorPowers 2 (n-1)) n k seed

--  2^s * d = n
-- k= number of trials
millerRabin' (s,d) n 0 seed = (ProbablyPrime,seed)
millerRabin' (s,d) n k seed | composite == Composite = (Composite,seed')
                            | otherwise = millerRabin' (s,d) n (k-1) seed'
                            where (composite,seed') =  checkCompositeness s d n seed



checkCompositeness s d n seed | x ==1 || x==n-1 = (ProbablyPrime,seed')
                              | otherwise = (checkCompositeness' s x n , seed')
                            where (a,seed') = generateRandomNumber (2,n-2) seed
                                  x = (modularPower d n a)



checkCompositeness' 0 x n   = Composite
checkCompositeness' s x n   | x == 1 = Composite
                             | x == n-1 = ProbablyPrime
                             | otherwise = checkCompositeness' (s-1) x' n
                            where x'= modularPower 2 n x

bigRandom = [7919,49979687]
{-              643808006803554439230129854961492699151386107534013432918073439524138264842370630061369715394739134090922937332590384720397133335969549256322620979036686633213903952966175107096769180017646161851573147596390153,
              614865347947412054998871912138166461049314312490725112873271296842414474254904548663296952332559110113044820194651149624596490803942453669130989178203975581114455363503708800628353373554758750709244049339485669,
              290245329165570025116016487217740287508837913295571609463914348778319654489118435855243301969001872061575755804802874062021927719647357060447135321577028929269578574760547268310055056867386875959045119093967972205124270441648450825188877095173754196346551 952542599226295413057787340278528252358809329]
-}

testGcd = gcdx 8 20 == (4,-2,1)

testMillerBig = map (\n -> millerRabin n 1 (mkStdGen 0)) bigRandom
testMiller =  millerRabin 7 1 (mkStdGen 0) 

testGenerateRandomNumber = generateRandomNumber (2^512,2^1024) seed
                        where seed = mkStdGen 0


testCheckCompositeness n = (r,d,a)
                    where (s,d) = (factorPowers 2 n)
                          seed = mkStdGen 0
                          r = checkCompositeness s d n seed
                          a = modularPower d n 2

testCheckCompositeness2 n = checkCompositeness' s x n
                where (s,d) = (factorPowers 2 n)
                      x= 3


-- deterministic isPrime
isPrime n = n > 1 && factors n == [n]
factors n = unfoldr (\(d,n) -> listToMaybe [(x, (x, div n x)) | n > 1,
   x <- [d..isqrt n] ++ [n], rem n x == 0]) (2,n)
isqrt n = floor . sqrt . fromIntegral $ n
