using System;
using System.IO;
using Android.App;
using Android.OS;
using Android.Widget;
using MHike.model;

namespace MHike
{
    [Activity(Label = "Update Hike")]
    public class UpdateHikeActivity : Activity
    {
        private EditText updateHikeNameEditText;
        private EditText updateHikeLocationEditText;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.update_hike);

            updateHikeNameEditText = FindViewById<EditText>(Resource.Id.updateHikeNameEditText);
            updateHikeLocationEditText = FindViewById<EditText>(Resource.Id.updateHikeLocationEditText);
            // Initialize other EditText fields

            Button updateHikeButton = FindViewById<Button>(Resource.Id.updateHikeButton);
            updateHikeButton.Click += UpdateHikeButton_Click;

            int hikeId = Intent.GetIntExtra("HikeId", -1);

            HikeDbContext db = new HikeDbContext();
            Hike hike = db.GetHikeById(hikeId);

            if (hike != null)
            {
                // Populate EditText fields with existing hike details
                updateHikeNameEditText.Text = hike.Name;
                updateHikeLocationEditText.Text = hike.Location;
                // Populate other EditText fields with existing hike details
            }
            else
            {
                // Handle case where the hike with the specified ID is not found
            }
        }

        private void UpdateHikeButton_Click(object sender, EventArgs e)
        {
            string name = updateHikeNameEditText.Text;
            string location = updateHikeLocationEditText.Text;
            // Retrieve values from other EditText fields

            Hike hike = new Hike
            {
                Name = name,
                Location = location,
                // Set other properties for the hike
            };

            HikeDbContext db = new HikeDbContext();
            int rowsAffected = db.UpdateHikeDetail(hike);

            if (rowsAffected > 0)
            {
                // Handle successful hike update
            }
            else
            {
                // Handle case where hike update failed
            }
        }
    }
}
