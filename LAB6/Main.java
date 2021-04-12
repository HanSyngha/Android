class circle{
	double radius;
	public circle(){
		this.radius = 10;
	}
	public circle(double radius){
		this.radius = radius;
	}
	public double Area(){
		return radius * radius;
	}
	public double getPerimeter(){
		return radius;
	}
}

public class Main{
	public static void main(String[] args){
		circle c1 = new circle();
		circle c2 = new circle(5);
		System.out.printf("c1’s Area is: %fpi\nc1’s radius is: %fpi\n",c1.Area(),c1.getPerimeter());
		System.out.printf("c2’s Area is: %fpi\nc2’s radius is: %fpi\n",c2.Area(),c2.getPerimeter());
	}
}
