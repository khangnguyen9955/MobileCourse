using Android.App;
using Android.OS;
using Android.Widget;
using System.Collections.Generic;
using MHike.model;

namespace MHike
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class ShowHikesActivity : Activity
    {
        private ListView hikeListView;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.feed);

            // Initialize the ListView
            hikeListView = FindViewById<ListView>(Resource.Id.hikeListView);

            // Retrieve a list of hikes and populate the ListView
            HikeDbContext db = new HikeDbContext();
            List<Hike> hikes = db.GetAllHikes();

            // Implement an adapter to display the list of hikes in the ListView
            // Adapter implementation depends on your preferences (ArrayAdapter, BaseAdapter, etc.)
        }
    }
}