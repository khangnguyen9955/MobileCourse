using Android.App;
using Android.OS;
using Android.Widget;
using System;
using Java.Lang;
using MHike.model;
using String = System.String;

namespace MHike
{
    [Activity(Label = "Create Hike")]
    public class CreateHikeActivity : Activity
    {
        private EditText hikeNameEditText;
        private EditText hikeLocationEditText;
        private TextView hikeDateTextView;
        private RadioGroup parkingRadioGroup;
        private EditText hikeLengthEditText;
        private RadioButton easyRadioButton;
        private RadioButton mediumRadioButton;
        private RadioButton hardRadioButton;
        private EditText hikeDescriptionEditText;
        private Button createHikeButton;
        private Button chooseDateButton;
        private DateTime selectedDate = DateTime.MinValue;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.create_hike);

            hikeNameEditText = FindViewById<EditText>(Resource.Id.hikeNameEditText);
            hikeLocationEditText = FindViewById<EditText>(Resource.Id.hikeLocationEditText);
            hikeDateTextView = FindViewById<TextView>(Resource.Id.hikeDateTextView);
            parkingRadioGroup = FindViewById<RadioGroup>(Resource.Id.parkingRadioGroup);
            hikeLengthEditText = FindViewById<EditText>(Resource.Id.hikeLengthEditText);
            easyRadioButton = FindViewById<RadioButton>(Resource.Id.easyRadioButton);
            mediumRadioButton = FindViewById<RadioButton>(Resource.Id.mediumRadioButton);
            hardRadioButton = FindViewById<RadioButton>(Resource.Id.hardRadioButton);
            hikeDescriptionEditText = FindViewById<EditText>(Resource.Id.hikeDescriptionEditText);
            createHikeButton = FindViewById<Button>(Resource.Id.createHikeButton);
            chooseDateButton = FindViewById<Button>(Resource.Id.chooseDateButton);  
            
            chooseDateButton.Click += ChooseDateButton_Click;
            createHikeButton.Click += CreateHikeButton_Click;
        }

        private void ChooseDateButton_Click(object sender, EventArgs e)
        {
            DatePickerDialog datepicker = new DatePickerDialog(this, OnDateSet, DateTime.Now.Year, DateTime.Now.Month - 1, DateTime.Now.Day);
            datepicker.Show();
        }
        private void OnDateSet(object sender, DatePickerDialog.DateSetEventArgs e)
        {
            selectedDate = e.Date;
            hikeDateTextView.Text = selectedDate.ToShortDateString();
            hikeDateTextView.Visibility = Android.Views.ViewStates.Visible;
        }

        private void CreateHikeButton_Click(object sender, EventArgs e)
        {
            string hikeName = hikeNameEditText.Text;
            string hikeLocation = hikeLocationEditText.Text;
            int parkingSelected = parkingRadioGroup.CheckedRadioButtonId;
            bool parkingAvailable = parkingSelected == Resource.Id.parkingYesRadioButton;
            string hikeLengthString = hikeLengthEditText.Text;
            string difficultyLevel = GetSelectedDifficultyLevel();
            string description = hikeDescriptionEditText.Text;

            if (validateInput(hikeName, hikeLocation, difficultyLevel,hikeLengthString))
            {
                float hikeLength = Float.ParseFloat(hikeLengthString);
                Hike hike = new Hike
                {
                    Name = hikeName,
                    Location = hikeLocation,
                    Date = selectedDate,
                    ParkingAvailable = parkingAvailable,
                    Length = hikeLength,
                    DifficultyLevel = difficultyLevel,
                    Description = description
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.SetTitle("Confirm Hike Details");
                alert.SetMessage("Hike Name: " + hikeName + "\nHike Location: " + hikeLocation + "\nDate: " + selectedDate.ToShortDateString() +
                                 "\nParking Available: " + (parkingAvailable ? "Yes" : "No") + "\nLength: " + hikeLength + "\nDifficulty Level: " +
                                 difficultyLevel + "\nDescription: " + description);
                alert.SetPositiveButton("Confirm", (senderAlert, args) =>
                {
                    HikeDbContext db = new HikeDbContext();
                    db.SaveHikeDetail(hike);
                    StartActivity(typeof(ShowHikesActivity));
                });
                alert.SetNegativeButton("Cancel", (senderAlert, args) => { });
                Dialog dialog = alert.Create();
                dialog.Show();
            }


        }


        private bool validateInput(string hikeName, string hikeLocation, string difficultyLevel, string hikeLengthString)
        {
            int parkingAvailable = parkingRadioGroup.CheckedRadioButtonId;
            bool valid = true;
            if (string.IsNullOrEmpty(hikeName))
            {
                Toast.MakeText(this,"Please enter a hike name.", ToastLength.Short).Show();
                valid = false;
            }

            if (string.IsNullOrEmpty(hikeLocation))
            {
                Toast.MakeText(this, "Please enter a hike location.", ToastLength.Short).Show();
                valid = false;
            }

            if (selectedDate == DateTime.MinValue)
            {
                Toast.MakeText(this, "Please select a hike date.", ToastLength.Short).Show();
                valid = false;
            }
            
            if(string.IsNullOrEmpty(hikeLengthString))
            {
                Toast.MakeText(this,"Please enter a hike length.", ToastLength.Short).Show();
                valid = false;
            }

            if (parkingAvailable == -1)
            {
                Toast.MakeText(this,"Please select if parking is available.", ToastLength.Short).Show();
                valid = false;
            }

            if (string.IsNullOrEmpty(difficultyLevel))
            {
                Toast.MakeText(this,"Please select a difficulty level.", ToastLength.Short).Show();
                valid = false;
            }

            return valid;
        }
        
        private string GetSelectedDifficultyLevel()
        {
            if (easyRadioButton.Checked)
            {
                return "Easy";
            }
            else if (mediumRadioButton.Checked)
            {
                return "Medium";
            }
            else if (hardRadioButton.Checked)
            {
                return "Hard";
            }
            else
            {
                return string.Empty;
            }
        }
    }
}
