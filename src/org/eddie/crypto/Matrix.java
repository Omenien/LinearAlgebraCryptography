package org.eddie.crypto;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.fraction.FractionConversionException;

import java.util.Vector;

public class Matrix
{
    Vector<Vector<Fraction>> matrix;

    int rowCount;
    int columnCount;

    @SuppressWarnings("unused")
    public Matrix(double[][] matrixArray)
    {
        constructFromArray(matrixArray);
    }

    public Matrix(Fraction[][] fractionArray)
    {
        constructFromArray(fractionArray);
    }

    public Matrix(int[][] matrixArray)
    {
        constructFromArray(matrixArray);
    }

    public Matrix constructFromArray(Fraction[][] matrixArray)
    {
        rowCount = matrixArray.length;
        columnCount = matrixArray[0].length;

        matrix = new Vector<>();

        for(int y = 0; y < rowCount; y++)
        {
            matrix.add(new Vector<>());

            for(int x = 0; x < columnCount; x++)
            {
                matrix.get(y).add(matrixArray[y][x]);
            }
        }

        return this;
    }

    public Matrix constructFromArray(double[][] matrixArray)
    {
        rowCount = matrixArray.length;
        columnCount = matrixArray[0].length;

        matrix = new Vector<>();

        for(int y = 0; y < rowCount; y++)
        {
            matrix.add(new Vector<>());

            for(int x = 0; x < columnCount; x++)
            {
                try
                {
                    matrix.get(y).add(new Fraction(matrixArray[y][x]));
                }
                catch(FractionConversionException e)
                {
                    System.err.println(matrixArray[y][x] + " is not a valid input for org.apache.commons.math3.fraction.Fraction.");
                }
            }
        }

        return this;
    }

    public Matrix constructFromArray(int[][] matrixArray)
    {
        rowCount = matrixArray.length;
        columnCount = matrixArray[0].length;

        matrix = new Vector<>();

        for(int y = 0; y < rowCount; y++)
        {
            matrix.add(new Vector<>());

            for(int x = 0; x < columnCount; x++)
            {
                try
                {
                    matrix.get(y).add(new Fraction(matrixArray[y][x]));
                }
                catch(FractionConversionException e)
                {
                    System.err.println(matrixArray[y][x] + " is not a valid input for org.apache.commons.math3.fraction.Fraction.");
                }
            }
        }

        return this;
    }

    public Matrix multiplyByMatrix(Matrix b)
    {
        if(columnCount != b.rowCount)
        {
            System.out.println("Unable to multiply matrices, matrix A has " + columnCount + " columns and matrix B has  " + b.rowCount + " rows.");

            return null;
        }

        double[][] outputMatrix = new double[rowCount][b.columnCount];

        double sum = 0.0;

        for(int c = 0; c < rowCount; c++)
        {
            for(int d = 0; d < b.columnCount; d++)
            {
                for(int k = 0; k < b.rowCount; k++)
                {
                    sum = sum + matrix.get(c).get(k).doubleValue() * b.matrix.get(k).get(d).doubleValue();
                }

                outputMatrix[c][d] = sum;
                sum = 0.0;
            }
        }

        return new Matrix(outputMatrix);
    }

    // http://www.sanfoundry.com/java-program-find-inverse-matrix/
    public Matrix invert()
    {
        double[][] a = convertToArray();

        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for(int i = 0; i < n; ++i)
        {
            b[i][i] = 1;
        }

        // Transform the matrix into an upper triangle
        gaussian(a, index);

        // Update the matrix b[i][j] with the ratios stored
        for(int i = 0; i < n - 1; ++i)
        {
            for(int j = i + 1; j < n; ++j)
            {
                for(int k = 0; k < n; ++k)
                {
                    b[index[j]][k] -= a[index[j]][i] * b[index[i]][k];
                }
            }
        }

        // Perform backward substitutions
        for(int i = 0; i < n; ++i)
        {
            x[n - 1][i] = b[index[n - 1]][i] / a[index[n - 1]][n - 1];
            for(int j = n - 2; j >= 0; --j)
            {
                x[j][i] = b[index[j]][i];
                for(int k = j + 1; k < n; ++k)
                {
                    x[j][i] -= a[index[j]][k] * x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }

        return new Matrix(x);
    }

    // http://www.sanfoundry.com/java-program-find-inverse-matrix/
    public static void gaussian(double a[][], int index[])
    {
        int n = index.length;
        double c[] = new double[n];

        // Initialize the index
        for(int i = 0; i < n; ++i)
        {
            index[i] = i;
        }

        // Find the rescaling factors, one from each row
        for(int i = 0; i < n; ++i)
        {
            double c1 = 0;
            for(int j = 0; j < n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if(c0 > c1)
                {
                    c1 = c0;
                }
            }
            c[i] = c1;
        }

        // Search the pivoting element from each column
        int k = 0;
        for(int j = 0; j < n - 1; ++j)
        {
            double pi1 = 0;
            for(int i = j; i < n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if(pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            // Interchange rows according to the pivoting order
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for(int i = j + 1; i < n; ++i)
            {
                double pj = a[index[i]][j] / a[index[j]][j];

                // Record pivoting ratios below the diagonal
                a[index[i]][j] = pj;

                // Modify other elements accordingly
                for(int l = j + 1; l < n; ++l)
                {
                    a[index[i]][l] -= pj * a[index[j]][l];
                }
            }
        }
    }

    public boolean isInvertible()
    {
        double determinant = determinant(convertToArray(), rowCount);

        if(determinant == 0)
        {
            System.out.println("Matrix is not invertible, determinant is " + determinant);

            return false;
        }
        else
        {
            return true;
        }
    }

    public double determinant(double[][] A, int numElements)
    {
        double det;

        if(numElements == 1)
        {
            det = A[0][0];
        }
        else if(numElements == 2)
        {
            det = A[0][0] * A[1][1] - A[1][0] * A[0][1];
        }
        else
        {
            det = 0;
            for(int j1 = 0; j1 < numElements; j1++)
            {
                double[][] m = new double[numElements - 1][];
                for(int k = 0; k < (numElements - 1); k++)
                {
                    m[k] = new double[numElements - 1];
                }
                for(int i = 1; i < numElements; i++)
                {
                    int j2 = 0;
                    for(int j = 0; j < numElements; j++)
                    {
                        if(j == j1)
                        {
                            continue;
                        }
                        m[i - 1][j2] = A[i][j];
                        j2++;
                    }
                }
                det += Math.pow(-1.0, 1.0 + j1 + 1.0) * A[0][j1] * determinant(m, numElements - 1);
            }
        }
        return det;
    }

    public double[][] convertToArray()
    {
        double[][] output = new double[rowCount][columnCount];

        int currentRow = 0;

        for(Vector<Fraction> innerVector : matrix)
        {
            int currentColumn = 0;

            for(Fraction fraction : innerVector)
            {
                output[currentRow][currentColumn] = fraction.doubleValue();

                currentColumn++;
            }

            currentRow++;
        }

        return output;
    }

    public String toString()
    {
        return matrix.toString().replace("[[", "[").replace("]]", "]").replace("], ", "]\n");
    }
}