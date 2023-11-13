package org.example;

import java.math.BigInteger;

class EllipticCurveDiffieHellman {
    public static void main(String[] args) {
        // Replace these values with your actual elliptic curve parameters and point coordinates
        BigInteger a = new BigInteger("2");  // coefficient a
        BigInteger b = new BigInteger("2");  // coefficient b
        BigInteger p = new BigInteger("62948365567077381076785749437466289389"); // prime modulus
        BigInteger x = new BigInteger("19283739880924114996531797216199530358");  // x-coordinate of the point
        BigInteger y = new BigInteger("40955782276983534261924476760645212500");  // y-coordinate of the point
        BigInteger n = new BigInteger("12345678901234567890123456789"); // scalar

        // Perform scalar multiplication using Double-and-Add
        Point result = doubleAndAdd(x, y, a, p, n);

        // Print the result
        System.out.println("Result of scalar multiplication: (" + result.x + ", " + result.y + ")");
    }

    // Point class to represent coordinates (x, y) on the elliptic curve
    static class Point {
        BigInteger x;
        BigInteger y;

        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }

    // Double-and-Add algorithm for scalar multiplication on an elliptic curve
    private static Point doubleAndAdd(BigInteger x, BigInteger y, BigInteger a, BigInteger p, BigInteger n) {
        Point result = new Point(BigInteger.ZERO, BigInteger.ZERO);
        Point current = new Point(x, y);
        String binaryN = n.toString(2); // Convert n to binary representation
        System.out.println(binaryN);

        for (int i =  0; i < binaryN.length(); i++) {

            if (result.x.equals(BigInteger.ZERO) && result.y.equals(BigInteger.ZERO)){

                result = current;

            }
            else {

                    result = doublePoint(result, a, p); // Double the result


                if (binaryN.charAt(i) == '1') {
                    result = addPoints(result, current, a, p); // Add the current point if the bit is 1
                }
            }
        }

        return result;
    }

    // Double a point on the elliptic curve
    private static Point doublePoint(Point point, BigInteger a, BigInteger p) {
        if (point.y.equals(BigInteger.ZERO)) {
            System.out.println("here");
            return new Point(BigInteger.ZERO, BigInteger.ZERO); // Point at infinity
        }

        // Calculate the numerator and denominator separately for better readability
        BigInteger numerator = point.x.pow(2).multiply(new BigInteger("3")).add(a);
        numerator = numerator.mod(p);
        //System.out.println("nominador: "+numerator);
        BigInteger denominator = point.y.multiply(new BigInteger("2"));
        denominator = denominator.modInverse(p);
        //System.out.println("denominador: "+denominator);


// Calculate the slope
        BigInteger slope = numerator.multiply(denominator).mod(p);
        //System.out.println("valor de s: "+slope);

        // 3xna2+a/2y
        // Calculate the slope
        // Calculate the new x-coordinate
        BigInteger xResult = slope.pow(2).subtract(point.x.multiply(new BigInteger("2"))).mod(p);
        //System.out.println(xResult);
        // Calculate the new y-coordinate
        BigInteger yResult = (slope.multiply(point.x.subtract(xResult)).subtract(point.y)).mod(p);
        //System.out.println(yResult);
        return new Point(xResult, yResult);
    }

    // Add two points on the elliptic curve
    private static Point addPoints(Point point1, Point point2, BigInteger a, BigInteger p) {
        if (point1.x.equals(BigInteger.ZERO) && point1.y.equals(BigInteger.ZERO)) {
            return point2;
        } else if (point2.x.equals(BigInteger.ZERO) && point2.y.equals(BigInteger.ZERO)) {
            return point1;
        } else if (point1.x.equals(point2.x) && point1.y.equals(point2.y.negate().mod(p))) {
            return new Point(BigInteger.ZERO, BigInteger.ZERO); // Point at infinity
        }

        BigInteger nominador = point1.y.subtract(point2.y).mod(p);
        BigInteger denominador = point1.x.subtract(point2.x).modInverse(p);
        // Calculate the slope
        BigInteger slope = nominador.multiply(denominador).mod(p);

        // Calculate the new x-coordinate
        BigInteger xResult = slope.pow(2).subtract(point1.x).subtract(point2.x).mod(p);

        // Calculate the new y-coordinate
        BigInteger yResult = slope.multiply(point1.x.subtract(xResult)).subtract(point1.y).mod(p);
        //System.out.println(xResult);
        //System.out.println(yResult);
        return new Point(xResult, yResult);
    }
}
