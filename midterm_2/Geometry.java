package midterm_2;

class Point {
    int x = 0, y = 0;

    Point() {
    }

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ',' + y + ')';
    }

    int[] getXY() {
        return new int[] { x, y };
    }

    void setXY(int x, int y) {
        setX(x);
        setY(y);
    }

    double distance(int x, int y) {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    double distance(Point another) {
        return Math.sqrt(Math.pow(another.x - this.x, 2) + Math.pow(another.y - this.y, 2));
    }

    double distance() {
        return Math.sqrt(x * x + y * y);
    }
}

class Circle {
    Point center = new Point(0, 0);
    double radius = 1.0;

    Circle() {
    }

    Circle(int xCenter, int yCenter, double radius) {
        this.center = new Point(xCenter, yCenter);
        this.radius = radius;
    }

    Circle(Point center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    double getRadius() {
        return radius;
    }

    void setRadius(double r) {
        radius = r;
    }

    Point getCenter() {
        return center;
    }

    void setCenter(Point center) {
        this.center = center;
    }

    int getCenterX() {
        return center.x;
    }

    int getCenterY() {
        return center.y;
    }

    void setCenterX(int x) {
        center.x = x;
    }

    void setCenterY(int y) {
        this.center.y = y;
    }

    int[] getCenterXY() {
        return new int[] { center.x, center.y };
    }

    void setCenterXY(int x, int y) {
        center.setXY(x, y);
    }

    @Override
    public String toString() {
        return "Circle[center=(" + center.x + ',' + center.y + ",radius=" + radius + ')';
    }

    double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

    double getCircumference() {
        return 2 * Math.PI * radius;
    }

    double distance(Circle another) {
        return this.center.distance(another.center) - (this.radius + another.radius);
    }
}

class Circle2 {
    double radius = 1.0;
    String color = "red";

    Circle2() {
    }

    Circle2(double r) {
        radius = r;
    }

    Circle2(double r, String c) {
        radius = r;
        color = c;
    }

    double getRadius() {
        return radius;
    }

    void setRadius(double r) {
        radius = r;
    }

    String getColor() {
        return color;
    }

    void setColor(String c) {
        color = c;
    }

    @Override
    public String toString() {
        return "radius=" + radius + ",color=" + color;
    }

    double getArea() {
        return Math.PI * Math.pow(radius, 2);
    }

}

class Cylinder extends Circle2 {
    double height = 1.0;

    Cylinder() {
    }

    Cylinder(double r, double h) {
        super(r);
        height = h;
    }

    Cylinder(double r, double h, String c) {
        super(r, c);
        height = h;
    }

    double getHeight() {
        return height;
    }

    void setHeight(double h) {
        height = h;
    }

    @Override
    public String toString() {
        return super.toString() + ",height=" + height;
    }

    double getVolume() {
        return Math.PI * Math.pow(radius, 2) * height;
    }
}
