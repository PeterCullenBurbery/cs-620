using System;
using System.Collections.Generic;

namespace IntervalPartitioning
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

        static int MinClassrooms(List<Interval> intervals)
        {
            if (intervals.Count == 0) return 0;

            // Sort intervals by start time
            intervals.Sort((a, b) => a.Start.CompareTo(b.Start));

            // Priority queue (min-heap) to track end times of rooms
            var minHeap = new List<int>();

            // Add the end time of the first interval
            minHeap.Add(intervals[0].End);

            for (int i = 1; i < intervals.Count; i++)
            {
                // If the current interval starts after or when a room becomes free (min end time)
                if (intervals[i].Start >= minHeap[0])
                {
                    // Remove the earliest ending room (reuse the room)
                    minHeap.RemoveAt(0);
                }

                // Allocate a new room (or reuse the existing one)
                minHeap.Add(intervals[i].End);

                // Ensure the heap is sorted by the end times
                minHeap.Sort();
            }

            // The number of rooms allocated is the size of the heap
            return minHeap.Count;
        }

        static void Main(string[] args)
        {
            // Define a list of intervals (lectures)
            List<Interval> intervals = new List<Interval>
            {
                new Interval(900, 1030),  // a
                new Interval(900, 1230),  // b
                new Interval(900, 1030),  // c
                new Interval(1100, 1230), // d
                new Interval(1100, 1400), // e
                new Interval(1300, 1430), // f
                new Interval(1300, 1430), // g
                new Interval(1400, 1630), // h
                new Interval(1500, 1630), // i
                new Interval(1500, 1630), // j
            };

            // Calculate the minimum number of classrooms required
            int requiredRooms = MinClassrooms(intervals);

            // Output the result
            Console.WriteLine($"Minimum number of classrooms required: {requiredRooms}");
        }
    }
}
