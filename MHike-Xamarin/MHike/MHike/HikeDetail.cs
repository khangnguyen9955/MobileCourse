using System.IO;
using Android.App;
using Android.OS;
using Android.Widget;
using MHike.model;

namespace MHike
{
    [Activity(Label = "Hike Detail")]
    public class GetHikeDetailActivity : Activity
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.hike_detail);

            int hikeId = Intent.GetIntExtra("HikeId", -1);
            

            HikeDbContext db = new HikeDbContext();
            Hike hike = db.GetHikeById(hikeId);

            if (hike != null)
            {
                // Populate TextViews with hike details
                TextView hikeNameTextView = FindViewById<TextView>(Resource.Id.hikeNameTextView);
                TextView hikeLocationTextView = FindViewById<TextView>(Resource.Id.hikeLocationTextView);

                hikeNameTextView.Text = hike.Name;
                hikeLocationTextView.Text = hike.Location;
                // Populate other TextViews for additional hike details
            }
            else
            {
                // Handle case where the hike with the specified ID is not found
            }
        }
    }
}