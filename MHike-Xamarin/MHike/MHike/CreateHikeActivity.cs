using System;
using System.IO;
using Android.App;
using Android.OS;
using Android.Widget;
using MHike.model;

namespace MHike
{
    [Activity(Label = "Create Hike")]
    public class CreateHikeActivity : Activity
    {
        private EditText hikeNameEditText;
        private EditText hikeLocationEditText;
        // Add more EditText fields for other hike details

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.create_hike);

            hikeNameEditText = FindViewById<EditText>(Resource.Id.hikeNameEditText);
            hikeLocationEditText = FindViewById<EditText>(Resource.Id.hikeLocationEditText);
            // Initialize other EditText fields

            Button createHikeButton = FindViewById<Button>(Resource.Id.createHikeButton);
            createHikeButton.Click += CreateHikeButton_Click;
        }

        private void CreateHikeButton_Click(object sender, EventArgs e)
        {
            string name = hikeNameEditText.Text;
            string location = hikeLocationEditText.Text;
            // Retrieve values from other EditText fields

            Hike hike = new Hike
            {
                Name = name,
                Location = location,
                // Set other properties for the hike
            };

            HikeDbContext db = new HikeDbContext();
            int rowsAffected = db.SaveHikeDetail(hike);

            if (rowsAffected > 0)
            {
                // Handle successful hike creation
            }
            else
            {
                // Handle case where hike creation failed
            }
        }
    }
}