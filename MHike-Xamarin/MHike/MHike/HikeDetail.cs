using Android.App;
using Android.Content;
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
            Android.Util.Log.Debug("HikeDetail", "Received HikeId: " + hikeId);

            HikeDbContext db = new HikeDbContext();
            Hike hike = db.GetHikeById(hikeId);

            if (hike != null)
            {
                TextView hikeNameTextView = FindViewById<TextView>(Resource.Id.hikeNameTextView);
                TextView hikeLocationTextView = FindViewById<TextView>(Resource.Id.hikeLocationTextView);
                TextView hikeDateTextView = FindViewById<TextView>(Resource.Id.hikeDateTextView);
                TextView hikeParkingTextView = FindViewById<TextView>(Resource.Id.hikeParkingAvailableTextView);
                TextView hikeLengthTextView = FindViewById<TextView>(Resource.Id.hikeLengthTextView);
                TextView hikeDifficultyTextView = FindViewById<TextView>(Resource.Id.hikeDifficultyLevelTextView);
                TextView hikeDescriptionTextView = FindViewById<TextView>(Resource.Id.hikeDescriptionTextView);

                hikeNameTextView.Text = hike.Name;
                hikeLocationTextView.Text = hike.Location;
                hikeDateTextView.Text = hike.Date.ToShortDateString();
                hikeParkingTextView.Text = hike.ParkingAvailable ? "Yes" : "No";
                hikeLengthTextView.Text = hike.Length.ToString();
                hikeDifficultyTextView.Text = hike.DifficultyLevel.ToString();
                hikeDescriptionTextView.Text = hike.Description;
                
                Button editHikeButton = FindViewById<Button>(Resource.Id.editHikeButton);
                Button deleteHikeButton = FindViewById<Button>(Resource.Id.deleteHikeButton);

                editHikeButton.Click += (sender, e) =>
                {
                    Intent editIntent = new Intent(this, typeof(UpdateHikeActivity));
                    editIntent.PutExtra("HikeId", hike.Id);
                    StartActivity(editIntent);
                };

                deleteHikeButton.Click += (sender, e) =>
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.SetTitle("Confirm Deletion");
                    alert.SetMessage("Are you sure you want to delete this hike?");
                    alert.SetPositiveButton("Yes", (senderAlert, args) =>
                    {
                        db.DeleteHikeDetail(hike);
                        Intent showHikesIntent = new Intent(this, typeof(ShowHikesActivity));
                        StartActivity(showHikesIntent);
                    });
                    alert.SetNegativeButton("No", (senderAlert, args) => { });
                    Dialog dialog = alert.Create();
                    dialog.Show();
                };

            }
            else
            {
                Toast.MakeText(this, "Hike not found", ToastLength.Short).Show();
            }
        }
    }
}