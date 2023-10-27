using System;

namespace MHike.model
{
    public class Hike
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Location { get; set; }
        public DateTime Date { get; set; }
        public bool ParkingAvailable { get; set; }
        public double Length { get; set; }
        public string DifficultyLevel { get; set; }
        public string Description { get; set; }
    }

}