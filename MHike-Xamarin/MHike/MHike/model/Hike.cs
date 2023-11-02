using System;
using SQLite;

namespace MHike.model
{
    public class Hike
    {
        [PrimaryKey, AutoIncrement]
        public int Id { get; set; }
        public string Name { get; set; }
        public string Location { get; set; }
        public DateTime Date { get; set; }
        public bool ParkingAvailable { get; set; }
        public float Length { get; set; }
        public string DifficultyLevel { get; set; }
        public string Description { get; set; }
    }

}