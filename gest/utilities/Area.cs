namespace utilities{
  public struct Area{
    public float x1;
    public float y1;
    public float x2;
    public float y2;

    public override string ToString(){
      return "[ " + topLeft() + "/" + bottomRight() + "]";
    }

    public Area(float x1, float y1, float x2, float y2){
      this.x1 = x1;
      this.x2 = x2;
      this.y1 = y1;
      this.y2 = y2;
    }

    public Position topLeft(){
      return new Position(x1, y1);
    }

    public Position bottomRight(){
      return new Position(x2, y2);
    }

    public float width(){
      return x2 - x1;
    }

    public float height(){
      return y2 - y1;
    }
  }
}