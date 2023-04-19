package practice_w4;

class Complex {
    double real = 0;
    double imagineUnit = 0;

    Complex() {
    }

    Complex(double real, double imagineUnit) {
        this.real = real;
        this.imagineUnit = imagineUnit;
    }

    Complex(Complex c) {
        this.real = c.real;
        this.imagineUnit = c.imagineUnit;
    }

    double getReal() {
        return real;
    }

    double getImagineUnit() {
        return imagineUnit;
    }

    Complex getComplex() {
        return new Complex(real, imagineUnit);
    }

    @Override
    public String toString() {
        return ("" + real
                + (imagineUnit != 0 ? ((imagineUnit > 0 ? " + " : " - ") + 'i' + Math.abs(imagineUnit)) : ""));
    }

    Complex add(Complex other) {
        return new Complex(real + other.real, imagineUnit + other.imagineUnit);
    }

    Complex sub(Complex other) {
        return new Complex(real - other.real, imagineUnit - other.imagineUnit);
    }

    Complex mul(Complex other) {
        return new Complex(real * other.real - imagineUnit * other.imagineUnit,
                real * other.imagineUnit + imagineUnit * other.real);
    }

    Complex div(Complex other) {
        double denominator = Math.pow(other.real, 2) + Math.pow(other.imagineUnit, 2);
        return new Complex(
                (real * other.real + imagineUnit * other.imagineUnit) / denominator,
                (imagineUnit * other.real - real * other.imagineUnit) / denominator);
    }

    static Complex pow(Complex c, int power) {
        Complex c1 = new Complex(c);
        for (int i = 1; i < power; i++)
            c1 = c1.mul(c);
        return c1;
    }
}

public class ComplexNumber {
    public static void main(String[] args) {
        Complex a = new Complex(1, 2);
        Complex b = new Complex(-3, 4);
        System.out.println(a);
        System.out.println(a.add(b));
        System.out.println(a.sub(b));
        System.out.println(a.mul(b));
        System.out.println(a.div(b));
        System.out.println(Complex.pow(a, 2));
    }
}
