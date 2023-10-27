using System.Collections.Generic;
using System.IO;
using System.Linq;
using MHike.model;
using SQLite;


namespace MHike
{
    public class HikeDbContext
    {
        private SQLiteConnection database;
        private string dbPath = Path.Combine(System.Environment.GetFolderPath (System.Environment.SpecialFolder.Personal),"mhike-database.db3");

        public HikeDbContext()
        {
            database = new SQLiteConnection(dbPath);
            database.CreateTable<Hike>();
        }

        public List<Hike> GetAllHikes()
        {
            return database.Table<Hike>().ToList();
        }
        
        public Hike GetHikeById(int id)
        {
            return database.Table<Hike>().FirstOrDefault(x => x.Id == id);
        }

        public int SaveHikeDetail(Hike hike)
        {
            return database.Insert(hike);
        }

        public int UpdateHikeDetail(Hike hike)
        {
            return database.Update(hike);
        }

        public int DeleteHikeDetail(Hike hike)
        {
            return database.Delete(hike);
        }

        public void DeleteAllHikeDetails()
        {
            database.DeleteAll<Hike>();
        }
    }

}