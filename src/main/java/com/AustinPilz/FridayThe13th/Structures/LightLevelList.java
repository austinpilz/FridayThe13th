package com.AustinPilz.FridayThe13th.Structures;

public class LightLevelList
{
    private DoublyLinkedList list = new DoublyLinkedList();
    private int maxSize;


    public LightLevelList(int maxSize)
    {
        this.maxSize = maxSize;
    }

    public void addLevel(Double newLevel)
    {
        list.addLast(newLevel);

        //Remove old light levels
        if (list.size() > maxSize)
        {
            list.removeFirst();
        }
    }

    public Double getAverage()
    {
        return list.calculateAverage();
    }
}
