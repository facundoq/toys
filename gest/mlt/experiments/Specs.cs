using System;
using System.Collections.Generic;
using System.Linq;
using utilities;

namespace mlt.experiments{
  public class Var{
    public string name;
    public IList<Object> values;

    public Var(string name, params Object[] values)
      : this(name, values.ToList()){
    }

    public Var(string name, IList<Object> values){
      this.name = name;
      this.values = values;
    }
  }

  public class Specs<T>{
    public List<Var> variables;

    public Specs(IList<Var> variables){
      this.variables = variables.ToList();
    }

    public Specs() : this(new List<Var>()){
    }

    public Specs<T> add(string name, params Object[] values){
      return add(name, values.ToList());
    }

    public Specs<T> add(string name, IEnumerable<object> values){
      variables.Add(new Var(name, values.ToList()));
      return this;
    }

    public List<T> generate(){
      return generate(variables).Select(ObjectInflator.inflate<T>).ToList();
    }

    private List<IDictionary<string, object>> generate(List<Var> variables){
      if (variables.Count == 0){
        return new List<IDictionary<string, object>>().add(new Dictionary<string, object>());
      }
      else{
        Var v = variables.removeAt(0);
        return generate(variables).SelectMany(d => v.values.Select(value =>{
          var result = d.clone();
          result[v.name] = value;
          return result;
        })
          ).ToList();
      }
    }
  }
}