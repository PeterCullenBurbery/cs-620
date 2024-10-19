using System;
using System.Collections.Generic;

namespace IntervalScheduling
{
    internal class Program
    {
        public class Interval
        {
            public int Start { get; set; }
            public int End { get; set; }

            public Interval(int start, int end)
            {
                Start = start;
                End = end;
            }

            public override string ToString()
            {
                return $"[{Start}, {End}]";
            }
        }

        static List<Interval> MaximizeIntervals(List<Interval> intervals)
        {
            // Sort intervals by their end time (finish time)
            intervals.Sort((a, b) => a.End.CompareTo(b.End));

            List<Interval> result = new List<Interval>();
            int lastEndTime = -1;

            foreach (var interval in intervals)
            {
                // Select the interval if it starts after the last selected interval ends
                if (interval.Start >= lastEndTime)
                {
                    result.Add(interval);
                    lastEndTime = interval.End;
                }
            }

            return result;
        }

        static void Main(string[] args)
        {
            List<Interval> intervals = new List<Interval>
            {
                new Interval(1,4),//B
                new Interval(3,5),//C
                new Interval(0,6),//A
                new Interval(4,7),//E
                new Interval(3,8),//D
                new Interval(5,9),//F
                new Interval(6,10),//G
                new Interval(8,11)//H
            };

            List<Interval> selectedIntervals = MaximizeIntervals(intervals);

            Console.WriteLine("Selected Intervals:");
            foreach (var interval in selectedIntervals)
            {
                Console.WriteLine(interval);
            }
        }
    }
}