package org.eddie.crypto;

public class Coordinate
{
    int row;
    int col;

    public Coordinate(int r, int c)
    {
        row = r;
        col = c;
    }

    public String toString()
    {
        return "(" + row + ", " + col + ")";
    }
}