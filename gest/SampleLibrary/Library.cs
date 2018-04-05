using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using utilities;

namespace SampleLibrary{
  public class Library{
    public static string defaultFolder = "library";
    public string folder;
    public List<Gesture> gestures;

    public Library(string folder){
      this.folder = folder;
      load();
    }

    public Library() : this(defaultFolder){
    }

    public Library load(){
      if (!Directory.Exists(folder)){
        Directory.CreateDirectory(folder);
      }
      gestures = Directory.EnumerateFiles(folder).Select(Extensions.deserialize<Gesture>).ToList();
      return this;
    }

    public void remove(Gesture gesture){
      gestures.Remove(gesture);
    }

    public void add(Gesture gesture){
      gestures.add(gesture);
    }

    public void save(){
      Directory.EnumerateFiles(folder).ToList().ForEach(File.Delete);
      gestures.ForEach(g => g.serialize(folder + "\\" + g.id + ".ges"));
    }

    public void clear(){
      gestures.Clear();
    }

    public void addAll(List<Gesture> gs){
      gestures.AddAll(gs);
    }
  }
}