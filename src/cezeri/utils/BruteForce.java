package cezeri.utils;

import java.util.Arrays;

public class BruteForce
{
    private char[] cs; // Characters
    private char[] gs; // Start from that size (Guess size)

    public BruteForce(char[] characterSet, int guessLength)
    {
        cs = characterSet;
        gs = new char[guessLength];
        Arrays.fill(gs, cs[0]);
    }

    public void increment()
    {
        int index = gs.length - 1;
        while (index >= 0)
        {
            if (gs[index] == cs[cs.length - 1])
            {
                if (index == 0)
                {
                    gs = new char[gs.length + 1];
                    Arrays.fill(gs, cs[0]);
                    break;
                } 
                else
                {
                    gs[index] = cs[0];
                    index--;
                }
            } 
            else
            {
                gs[index] = cs[Arrays.binarySearch(cs, gs[index]) + 1];
                break;
            }
        }
    }

    @Override
    public String toString()
    {
        return String.valueOf(gs);
    }
}
