package com.example.simon_says_app;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class UserButtonPresses
{
    private static Queue<Integer> m_Queue;
    private static boolean m_BlockingQueueSet=false;

    public static void SetNewBlockingQueue(int i_size)
    {
        m_Queue=new ArrayBlockingQueue<>(i_size);
        m_BlockingQueueSet=true;
    }

    public static boolean Add(int value) throws Exception {
        if(m_BlockingQueueSet)
        {
            return m_Queue.add(value);

        }
        else
            throw new Exception("blockingQueue size wasnt set");
    }
    public static int Remove() throws Exception {
        if(m_BlockingQueueSet)
        {
            return m_Queue.remove();

        }
        else
            throw new Exception("blockingQueue size wasnt set");
    }
    public static int GetSize()  {
        if(m_BlockingQueueSet)
        {
            return m_Queue.size();

        }
        else
           return 0;
    }
}