using System.Collections.Generic;

namespace mlt.cpn.cncoriginal{
  public class Permutador
  {
    public static void Permutar<T>(List<T> lista)
    {
      RandomGenerator rg=RandomGenerator.GetInstance();
      for(int i=0;i<lista.Count;i++)
      {
        int indice1=rg.Next(0, lista.Count - 1);
        int indice2=rg.Next(0, lista.Count - 1);
        T t=lista[indice1];
        lista[indice1]=lista[indice2];
        lista[indice2]=t;
      }
    }
  }
}
