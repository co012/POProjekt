package simulator;

import java.util.*;

public class Vector2d {
    public final int x;
    public final int y;

    public final static Vector2d ZERO = new Vector2d(0, 0);

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public static Vector2d getRandomVector(Vector2d from, Vector2d to) {
        if (from == null || to == null) throw new IllegalArgumentException("arguments can't be null");

        Random random = new Random();

        int x = random.nextInt(to.x - from.x);
        x += from.x;

        int y = random.nextInt(to.y - from.y);
        y += from.y;

        return new Vector2d(x, y);
    }


    public int toAreaOfRectangle() {
        return this.x * this.y;
    }


    @Override
    public String toString() {
        return "[" + x +
                "," + y +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2d add(Vector2d that) {
        return new Vector2d(this.x + that.x, this.y + that.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }

    public Vector2d subtract(Vector2d that) {
        return add(that.opposite());
    }

    public Vector2d moduloPositive(Vector2d that) {
        int newX = this.x % that.x;
        int newY = this.y % that.y;
        if (newX < 0) newX += that.x;
        if (newY < 0) newY += that.y;
        return new Vector2d(newX, newY);
    }


    public Vector2d yModulo(int m) {
        return new Vector2d(x, y % m);
    }

    public int toScalar(int n) {
        if (x >= n) throw new IllegalArgumentException("n must be grater than x");
        return n * y + x;
    }

    public Vector2d fromScalar(int scalar, int n) {
        return new Vector2d(scalar % n, scalar / n);
    }

    public Vector2d getLinearNext(int n) {
        return fromScalar(this.toScalar(n) + 1, n);
    }

    public Vector2d getLinearNextInRectangle(Vector2d lowerLeft, Vector2d upperRight) {
        Vector2d recTranslatedTo0 = upperRight.subtract(lowerLeft);
        Vector2d thisTranslated = this.subtract(lowerLeft);
        Vector2d translatedLinearNext = thisTranslated.getLinearNext(recTranslatedTo0.x);
        translatedLinearNext = translatedLinearNext.yModulo(recTranslatedTo0.y);
        return translatedLinearNext.add(lowerLeft);

    }

    public boolean isInsideRectangle(Vector2d lowerLeft, Vector2d upperRight) {
        boolean xIn = lowerLeft.x <= this.x && this.x < upperRight.x;
        boolean yIn = lowerLeft.y <= this.y && this.y < upperRight.y;
        return xIn && yIn;
    }


}
