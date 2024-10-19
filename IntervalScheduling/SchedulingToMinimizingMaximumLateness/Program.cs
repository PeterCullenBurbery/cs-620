using System;
using System.Collections.Generic;

namespace SchedulingToMinimizingMaximumLateness
{
    internal class Program
    {
        public class Job
        {
            public int JobNumber { get; set; }
            public int ProcessingTime { get; set; }
            public int Deadline { get; set; }

            public Job(int jobNumber, int processingTime, int deadline)
            {
                JobNumber = jobNumber;
                ProcessingTime = processingTime;
                Deadline = deadline;
            }

            public override string ToString()
            {
                return $"Job {JobNumber}: Processing Time = {ProcessingTime}, Deadline = {Deadline}";
            }
        }

        static int MinimizeMaximumLateness(List<Job> jobs)
        {
            // Sort jobs by deadline (d_j)
            jobs.Sort((a, b) => a.Deadline.CompareTo(b.Deadline));

            int currentTime = 0;
            int maxLateness = 0;

            // Process each job
            foreach (var job in jobs)
            {
                // Start the job at the current time and compute its finish time
                currentTime += job.ProcessingTime;
                int lateness = Math.Max(0, currentTime - job.Deadline);  // Lateness for this job

                // Track the maximum lateness
                maxLateness = Math.Max(maxLateness, lateness);

                Console.WriteLine($"Job {job.JobNumber} finishes at time {currentTime} with lateness {lateness}");
            }

            return maxLateness;
        }

        static void Main(string[] args)
        {
            // Define a list of jobs with their processing times and deadlines
            List<Job> jobs = new List<Job>
            {
                new Job(1, 3, 1),  // Job 1: t1 = 3, d1 = 1
                new Job(2, 2, 3)   // Job 2: t2 = 2, d2 = 3
            };

            // Calculate the minimum maximum lateness
            int maxLateness = MinimizeMaximumLateness(jobs);

            // Output the result
            Console.WriteLine($"Minimum Maximum Lateness: {maxLateness}");
        }
    }
}
